package com.dreamcoffee.opencv.demo.task;

import com.dreamcoffee.opencv.demo.service.AuctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * ScheduledTasks
 *
 * @author chenhuihua
 * @date 2019/5/14
 */
@Component
public class ScheduledTasks {

    @Autowired
    private AuctionService auctionService;

    @Scheduled(cron = "0/5 * * * * *")
    public void auction() {
//        auctionService.refresh();
        int price = auctionService.getPrice();
//        auctionService.buy(price);
//        auctionService.sell();
    }
}
