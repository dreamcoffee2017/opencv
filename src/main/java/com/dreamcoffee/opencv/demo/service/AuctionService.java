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
     * 刷新
     */
    void refresh();

    /**
     * 查询价格
     *
     * @return
     */
    int getPrice();

    /**
     * 买
     *
     * @param price
     */
    void buy(int price);

    /**
     * 卖
     */
    void sell();
}
