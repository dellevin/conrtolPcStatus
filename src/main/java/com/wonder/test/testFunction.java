package com.wonder.test;

import com.wonder.utils.OshiTool;
import org.junit.Test;
import oshi.SystemInfo;

import java.io.IOException;

public class testFunction {
    @Test
    public  void shutDownTrue ( ) throws IOException {
        String str="1000";
        Runtime.getRuntime().exec("shutdown -s -t "+str); //1000是指1000秒
        System.out.println("执行关机");
    }
    @Test
    public  void shutDownFalse() throws IOException {
        Runtime.getRuntime().exec("shutdown -a");
        System.out.println("取消关机");
    }


}
