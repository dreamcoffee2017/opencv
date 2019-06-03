package com.dreamcoffee.opencv.demo;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Application
 *
 * @author chenhuihua
 * @date 2019/5/13
 */
@SpringBootApplication
@EnableScheduling
public class Application {

    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .sources(Application.class)
                .headless(false)
                .run();
    }

}
