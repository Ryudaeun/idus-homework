package com.idus.homework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class IdusHomeworkApplication {

    public static void main(String[] args) {
        SpringApplication.run(IdusHomeworkApplication.class, args);
    }

}
