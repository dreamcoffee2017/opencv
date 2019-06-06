package com.dreamcoffee.opencv.demo.task;

import com.dreamcoffee.opencv.demo.util.ImgUtil;
import org.junit.Test;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * ScheduledTasksTest
 *
 * @author Administrator
 * @date 2019/6/5
 */
public class ScheduledTasksTest {

    @Test
    public void printScreen() throws Exception {
        Robot robot = new Robot();
        BufferedImage bufImg = robot.createScreenCapture(new Rectangle(182, 355, 107, 13));
        Mat src = ImgUtil.imgToMat(bufImg);
        HighGui.imshow("", src);
        HighGui.waitKey();
    }
}