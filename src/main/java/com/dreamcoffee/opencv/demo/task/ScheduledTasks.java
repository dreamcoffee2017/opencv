package com.dreamcoffee.opencv.demo.task;

import com.dreamcoffee.opencv.demo.util.ImgUtil;
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

    @Scheduled(cron = "0/5 * * * * *")
    public void printScreen() throws Exception {
        // 搜索，xy
        // 截图
        Robot robot = new Robot();
        BufferedImage bufImg = robot.createScreenCapture(new Rectangle(182, 355, 107, 13));
        // 识别第一组数据，xy
        Integer result = ImgUtil.compare(bufImg);
        System.out.println(result);
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
