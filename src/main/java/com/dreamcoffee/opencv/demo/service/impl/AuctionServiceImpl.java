package com.dreamcoffee.opencv.demo.service.impl;

import com.dreamcoffee.opencv.demo.service.AuctionService;
import com.dreamcoffee.opencv.demo.util.ImgUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;

/**
 * AuctionServiceImpl
 *
 * @author Administrator
 * @date 2019/6/14
 */
@Service
public class AuctionServiceImpl implements AuctionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuctionServiceImpl.class);

    @Autowired
    private Robot robot;

    @Override
    public void init() {

    }

    @Override
    public void open() {

    }

    @Override
    public void close() {

    }

    @Override
    public void refresh() {
        robot.mouseMove(1, 2);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
    }

    @Override
    public int getPrice() {
        BufferedImage bufImg = robot.createScreenCapture(new Rectangle(182, 355, 107, 13));
        Integer result = ImgUtil.compare(bufImg);
        Assert.notNull(result, "比对失败");
        LOGGER.info("比对结果 {}", result);
        return result;
    }

    @Override
    public void buy(int price) {
        int maxPrice = 300;
        if (price < maxPrice) {
            robot.mouseMove(1, 2);
            robot.mousePress(InputEvent.BUTTON1_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_MASK);
            robot.mousePress(InputEvent.BUTTON1_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_MASK);
        }
    }

    @Override
    public void sell() {
    }
}
