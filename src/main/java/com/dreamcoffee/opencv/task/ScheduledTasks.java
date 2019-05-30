package com.dreamcoffee.opencv.task;

import com.dreamcoffee.opencv.util.ImgUtil;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
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
        if (1 == 1) {
            BufferedImage bufImg = ImgUtil.getImgFromClip();
            if (bufImg != null) {
                Mat mat = ImgUtil.imgToMat(bufImg);
                HighGui.imshow("", mat);
                HighGui.waitKey();
            }
            return;
        }
        // 搜索，xy
        // 截图
        Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_PRINTSCREEN);
        robot.keyRelease(KeyEvent.VK_PRINTSCREEN);
        // 识别第一组数据，xy
//        String filename = "C:/Users/Administrator/Documents/地下城与勇士/ScreenShot";
        String filename = "D:/work/project/opencv/template/template.jpg";
        String templateDir = "D:/work/project/opencv/template/";
        int result = ImgUtil.compare(filename, templateDir, Imgproc.THRESH_BINARY);
        System.out.println(result);
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
