package com.ycmm.common.utils;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;

public class IdKit {

    private static ConcurrentLinkedQueue<Integer> queue = new ConcurrentLinkedQueue<>();
    private static CountDownLatch downLatch = new CountDownLatch(1);

    /**
     * 获取通用ID  参考MongoDB的ObjectId
     * @return
     */
    public static String getUniversalId() {
        return ObjectId.get().toHexString();
    }

    /**
     * 每毫秒生成的 ID 最大值，约定取整百、整千、整万
     */
    public static final int maxPerMSECNum = 10000;

    /**
     * 初始化 队列数据
     */
    public static void init() {
        for (int i = 0; i < maxPerMSECNum; i++) {
            queue.add(i);
        }
        downLatch.countDown();
    }

    public static Integer poll() {
        try {
            if (downLatch.getCount() > 0) {
                init();
                Thread.sleep(1000);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //取出头部数据
        Integer i = queue.poll();
        //重新添加到尾部
        queue.offer(i);
        return i;
    }

    /**
     * 获取数字ID，根据时间戳 + maxPerMSECNum
     * @return
     */
    public static String getNumberId() {
        //获取时间戳
        long currentTimeMillis = System.currentTimeMillis();
        int number = maxPerMSECNum + poll();

        return currentTimeMillis + String.valueOf(number);

    }
}
