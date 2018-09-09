package com.ycmm.common.designpattern.proxy.jdk;

/**
 * 性能监控器
 * @Author Oliver.qin
 * @Date 2018/8/3 09:27
 * @Description:
 */

public class PerformanceMonitor {

    //通过一个ThreadLocal保存调用线程相关的性能监视信息
    private static ThreadLocal<MethodPerformance> threadLocal = new ThreadLocal<MethodPerformance>();

    //启动对某一目标方法的监控
    public static void begin(String method) {
        System.out.println("begin monitor ...");
        MethodPerformance methodPerformance = new MethodPerformance(method);
        threadLocal.set(methodPerformance);
    }

    public static void end() {
        System.out.println("end monitor ...");
        MethodPerformance mp = threadLocal.get();
        mp.printPerformance();
    }
}
