package com.wyr.my_class_project;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.wyr.my_class_project.mapper")
@EnableScheduling
public class MyClassProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyClassProjectApplication.class, args);
    }
}
