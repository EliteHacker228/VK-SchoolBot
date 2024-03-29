package com.max.example.files;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@SpringBootApplication(scanBasePackages = {"com.max.example.files.datanodes", "com.max.example.files"})
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

}
