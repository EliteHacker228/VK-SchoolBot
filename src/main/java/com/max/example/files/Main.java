package com.max.example.files;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.max.example.files.datanodes", "com.max.example.files"})
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
