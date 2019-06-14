package com.dreamcoffee.opencv.demo.service;

/**
 * AuctionService
 *
 * @author Administrator
 * @date 2019/6/14
 */
public interface AuctionService {

    /**
     * 初始化坐标
     */
    void init();

    /**
     * 打开
     */
    void open();

    /**
     * 关闭
     */
    void close();

    /**
     * 买
     */
    void buy();

    /**
     * 卖
     */
    void sell();
}
