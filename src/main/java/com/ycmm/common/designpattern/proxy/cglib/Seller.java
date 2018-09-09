package com.ycmm.common.designpattern.proxy.cglib;

/**
 * @Author Oliver.qin
 * @Date 2018/8/2 17:36
 * @Description:
 */
public class Seller{

    public void sellTicket(int ticketNum) {
        System.out.println("开始卖票");
        System.out.println(1/0);
        System.err.println("模拟已经卖出" + ticketNum +"张票");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
