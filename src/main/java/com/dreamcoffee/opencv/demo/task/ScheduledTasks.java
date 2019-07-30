package com.dreamcoffee.opencv.demo.task;

import com.dreamcoffee.opencv.demo.service.AuctionService;
import com.dreamcoffee.opencv.demo.util.ImgUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;

/**
 * ScheduledTasks
 *
 * @author chenhuihua
 * @date 2019/5/14
 */
@Component
public class ScheduledTasks {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledTasks.class);

    @Autowired
    private Robot robot;
    @Autowired
    private AuctionService auctionService;

    @Scheduled(cron = "0/5 * * * * *")
    public void printScreen() throws Exception {
        // 搜索，xy
        // 截图
        BufferedImage bufImg = robot.createScreenCapture(new Rectangle(182, 355, 107, 13));
        // 识别第一组数据，xy
        Integer result = ImgUtil.compare(bufImg);
        LOGGER.info("比对结果 {}", result);
        if (result == null) {
            return;
        }
        // 购买，xy
        int price = 300;
        if (result < price) {
            robot.mouseMove(1, 2);
            robot.mousePress(InputEvent.BUTTON1_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_MASK);
            robot.mousePress(InputEvent.BUTTON1_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_MASK);
        }
        // 出售，xy
    }

}
