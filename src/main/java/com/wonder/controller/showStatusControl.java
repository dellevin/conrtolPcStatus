package com.wonder.controller;

import com.alibaba.fastjson.JSON;
import com.wonder.entity.TestTable;
import com.wonder.entity.Tt2;
import com.wonder.mapper.TestTableMapper;
import com.wonder.mapper.Tt2Mapper;
import com.wonder.service.TestTableService;
import com.wonder.utils.OshiTool;
import com.wonder.utils.ProcList;
import com.wonder.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.web.bind.annotation.*;
import oshi.SystemInfo;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@CrossOrigin
public class showStatusControl {
    @RequestMapping("/hello")
    public String test()
    {
        return "hello";
    }
    @RequestMapping(method = RequestMethod.GET,value = "/getSysInfo")
    public String getSysInfo()
    {
        SystemInfo si = new SystemInfo();
        Map<String,Object> sysInfo =OshiTool.getSysInfo(si);
        String sysInfoJson= JSON.toJSONString(sysInfo);
        return sysInfoJson;
    }
    @RequestMapping(value = "/getCpuInfo",method = RequestMethod.GET)
    public String getCpuInfo()
    {
        SystemInfo si = new SystemInfo();
        Map<String,Object> sysInfo =OshiTool.getCpuInfo(si);
        String cpuInfoJson= JSON.toJSONString(sysInfo);
        return cpuInfoJson;
    }
    @RequestMapping(value ="/isShutDownPc" , method = RequestMethod.POST)
    public void shutDownPc(@RequestBody String message) throws IOException {
        //System.out.println(message);  //我不理解为啥会多出来一个等于号
        if (message.equals("shutdown=")) {
            String str="1000";
            Runtime.getRuntime().exec("shutdown -s -t "+str); //1000是指1000秒
            System.out.println("执行关机");
        }else{
            Runtime.getRuntime().exec("shutdown -a");
            System.out.println("取消关机");
        }
    }
    @RequestMapping(method = RequestMethod.GET,value = "/getPidAndName")
    public String getPidAndName()
    {
        ProcList projects = new ProcList();
        return projects.PidAndName();
    }
    @RequestMapping(value ="/killPid" , method = RequestMethod.POST)
    public void killPid(@RequestBody String message) throws IOException {
        // message  8812=
        message = message.substring(0,message.length() - 1);
        System.out.println(message);
        Runtime.getRuntime().exec("taskkill -f -PID "+message); //1000是指1000秒
    }

    @Resource
    Tt2Mapper tt2Mapper;
    @Resource
    TestTableMapper testTableMapper;


    @Autowired
    TestTableService testTableService;
    @PostMapping("/add")
    public boolean add(TestTable testTable){
        return  testTableService.add(testTable);

    }
    @GetMapping("/getsql/{id}")
    public Object selectById(@PathVariable("id") Integer id){
        //模拟创建线程
        ExecutorService es = Executors.newFixedThreadPool(200);
            es.submit(new Runnable() {
                @Override
                public void run() {
                    testTableService.getTestTableById(id);
                }
            });



        return  testTableService.getTestTableById(id);
    }






    @GetMapping("/listTestTable")
    public Result listTestTable() {
        // 返回所有
        List<TestTable> testTables = testTableMapper.selectList(null);
        System.out.println(testTables);
        return new Result().ok(testTables);
    }
    @GetMapping("/listTt2")
    public Result listTt2() {
        // 返回所有
        List<Tt2> tt2List = tt2Mapper.selectList(null);
        return new Result().ok(tt2List);
    }
    @PostMapping("/update")
    public Result update(Integer id,String email,String love) {
        Tt2 tt2 = new Tt2();
        tt2.setId(id);
        tt2.setEmail(email);
        tt2.setLove(love);
        int b = tt2Mapper.updateById(tt2);

        if (b > 0) {
            return new Result().ok("ok");
        }else {
            return new Result().error("please cheack msg");
        }

    }
    @PostMapping("/insertInfo")
    public Result update(String name,String tel) {
        TestTable result = new TestTable();
        result.setName(name);
        result.setTel(tel);
        testTableMapper.insert(result);
        return new Result().ok("ok");
    }


    @PostMapping("/del")
    public Result deleteById(Integer id){
        boolean b = testTableService.deleteById(id);
        if (b) {
            return new Result().ok(b);
        }else {
            return new Result().error("error");
        }

    }

    @Autowired
    private RedisTemplate redisTemplate;
    @PostMapping("/redisSet")
    public void redisSet(@RequestBody TestTable testTable){
        redisTemplate.opsForValue().set("TestTableKey",testTable);
    }
    @PostMapping("/redisSet/{key}/{value}")
    public void redisSet2(@PathVariable("key") String key,
                         @PathVariable("value") String value){
        redisTemplate.opsForValue().set(key,value);
    }

    @GetMapping("/get/{key}")
    public  Object getRedisKey(@PathVariable("key") String key){
        return redisTemplate.opsForValue().get(key);
    }

    @PostMapping("/delRedisKey/{key}")
    public boolean hasKey(@PathVariable("key") String key){
        redisTemplate.delete(key);
        return redisTemplate.hasKey(key);
    }

    @GetMapping("/string")
    public void saveString(){
        redisTemplate.opsForValue().set("sss","sssssss");
        System.out.println(redisTemplate.opsForValue().get("sss"));
    }
    @GetMapping("/list")
    public List<String> savelist() {
        ListOperations<String,String> listOperations = redisTemplate.opsForList();
        listOperations.leftPush("qqq","woshi");
        listOperations.leftPush("qqq","nidie");
        listOperations.leftPush("qqq","2333");
        listOperations.leftPush("qqq","3edcv");
        List<String> list = listOperations.range("qqq",0,-1);
        return list;
    }

    @GetMapping("/set")
    public Set<String> saveSet() {
        SetOperations setOperations = redisTemplate.opsForSet();
        setOperations.add("aaa","aaaaaaaaaa");
        setOperations.add("aaa","123");
        setOperations.add("aaa","123");
        Set<String> sets = setOperations.members("aaa");
        return sets;
    }

    @GetMapping("/zset")
    public Set<String> saveZset() {
        ZSetOperations zsetOperations = redisTemplate.opsForZSet();
        zsetOperations.add("aaaa","zzzz",1);
        zsetOperations.add("aaaa","123z",2);
        zsetOperations.add("aaaa","123z",3);
        Set<String> sets = zsetOperations.range("aaaa",0,-1);
        return sets;
    }
    @GetMapping("/hashset")
    public void saveHashSet() {
        HashOperations<String,String,String> hashOperations = redisTemplate.opsForHash();
        hashOperations.put("小明","今年","十八岁");
        System.out.println(hashOperations.get("小明", "今年"));
    }

}
