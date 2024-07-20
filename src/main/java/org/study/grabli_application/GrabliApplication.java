package org.study.grabli_application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"org.study.grabli_application"})
public class GrabliApplication {

    public static void main(String[] args) {
        SpringApplication.run(GrabliApplication.class, args);
    }
}
