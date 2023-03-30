package com.wonder.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wonder.entity.TestTable;
import com.wonder.mapper.TestTableMapper;
import com.wonder.service.TestTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author DelLevin
 * @since 2023-03-29
 */
@Service
public class TestTableServiceImpl extends ServiceImpl<TestTableMapper, TestTable> implements TestTableService {
    @Resource
    TestTableMapper testTableMapper;
    @Resource
    RedisTemplate redisTemplate;
    @Override
    public boolean deleteById(int id) {
        TestTable testTable = new TestTable();
        testTable.setId(id);
        int b = testTableMapper.deleteById(testTable);
        return b > 0 ? true: false;
    }
    @Override
    public boolean add(TestTable testTable) {
        int b = testTableMapper.insert(testTable);
        return b > 0 ? true: false;
    }
    @Override
    public TestTable selectById(Integer id) {
        return  testTableMapper.selectById(id);

    }
    @Override
    public Object getTestTableById(Integer id){
        String key = "name:"+id;
        Object object = redisTemplate.opsForValue().get(key);
        if (object == null) {
            //同步代码块
            synchronized (this.getClass()){
                object = redisTemplate.opsForValue().get(key);
                if(object == null){
                    System.out.println("查询数据库...........");
                    TestTable testTable = testTableMapper.selectById(id);
                    //设置缓存
                    redisTemplate.opsForValue().set(key, testTable);
                    return  testTable;
                }else {
                    System.out.println("查询缓存(tongbudaimakuai)>>>>>>>>>>>>>>>");
                    return object;
                }
            }
        }else {
            System.out.println("查询缓存>>>>>>>>>>>>>>>");
            return object;
        }
    }
}
