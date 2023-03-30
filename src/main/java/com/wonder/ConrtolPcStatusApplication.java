package com.wonder;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@MapperScan("com.wonder.mapper")
public class ConrtolPcStatusApplication {
    public static void main(String[] args) {
            SpringApplication.run(ConrtolPcStatusApplication.class, args);
    }

}
