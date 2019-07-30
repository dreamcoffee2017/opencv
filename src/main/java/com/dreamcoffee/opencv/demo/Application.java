package com.dreamcoffee.opencv.demo;

import com.dreamcoffee.opencv.demo.util.ImgUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.awt.*;

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
        new SpringApplicationBuilder(Application.class)
                .headless(false)
                .run();
    }

    /**
     * 模拟操作需要headless=false
     *
     * @return
     * @throws AWTException
     */
    @Bean
    public Robot robot() throws AWTException {
        return new Robot();
    }

    @Bean
    public CommandLineRunner run() {
        return args -> ImgUtil.initTemplate();
    }
}
