package com;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        String port = System.getenv("PORT");
        System.out.println("<<<<<<<<<<<<<<<<<<<MY PORT: "+port+" >>>>>>>>>>>>>>>>>>>");
        System.out.println("args:");
        for (String arg : args) {
            System.out.println(arg);
        }

        app.setDefaultProperties(Collections
                .singletonMap("server.port", port));
        //app.run(args);
        SpringApplication.run(Application.class, args);
    }


}