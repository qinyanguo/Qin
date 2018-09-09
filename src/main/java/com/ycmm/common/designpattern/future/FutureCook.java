package com.ycmm.common.designpattern.future;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @Author Oliver.qin
 * @Date 2018/8/1 15:06
 * @Description:
 * https://www.cnblogs.com/cz123/p/7693064.html
 */

public class FutureCook {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        long startTime = System.currentTimeMillis();

//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        };

        /**
         * Callable 接口类似于 Runnable，两者都是为那些其实例可能被另一个线程执行的类设计的
         * 但是 Runnable 不会返回结果，并且无法抛出经过检查的异常。
         */
        //网购厨具
        Callable<KitchenWare> onlineShopping = new Callable<KitchenWare>() {
            @Override
            public KitchenWare call() throws Exception {
                System.err.println("第一步：下单，等待送货");
                Thread.sleep(8000); //模拟送货时间
                System.out.println("第一步：快递送到");
                return new KitchenWare();
            }
        };

        FutureTask<KitchenWare> futureTask = new FutureTask<>(onlineShopping);
        new Thread(futureTask).start();


        //第二步  去超市购买食材
        Thread.sleep(2000);  // 模拟购买食材时间，如果，购买食材时间 > 送货时间，则食材先到位
        FoodMaterial foodMaterial = new FoodMaterial();
        System.out.println("第二步：食材到位");


        //第三步：用厨具烹饪食材
        if(!futureTask.isDone()) { //联系快递员是否到货   如果任务已完成，则返回 true
            System.out.println("厨具还没到，先吃个冰淇淋等着（如果心情不好就调用 cancel 方法取消订单，就取消了哈哈哈）");
        }

       /**
        *  仅在计算完成时才能获取结果；如果计算尚未完成，则阻塞 get 方法。
        *  get方法：获取计算结果（如果还没计算完，也是必须等待的）
        *  cancel方法：还没计算完，可以取消计算过程
        *  isDone方法：判断是否计算完
        *  isCancelled方法：判断计算是否被取消
        */
        KitchenWare kitchenWare = futureTask.get();
        System.out.println("第三步：厨具已到位，食材早备好，准备");
        Thread.sleep(2000); //洗刷厨具
        cook(kitchenWare, foodMaterial);

    }

    //用厨具烹饪食材
    static void cook(KitchenWare kitchenWare, FoodMaterial foodMaterial) {
        System.err.println("第四步：四季时蔬拼盘 + 香气腾腾的鱼肉");
    }

    //厨具类
    static class KitchenWare {}

    //食材类
    static class FoodMaterial {}

}
