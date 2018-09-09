package com.ycmm.common.designpattern.proxy.jdk;

/**
 * @Author Oliver.qin
 * @Date 2018/8/3 11:29
 * @Description:
 */

public class MethodPerformance {

    private long begin;

    private long end;

    private String serviceMethod;

    public MethodPerformance(String serviceMethod) {
        this.begin = System.currentTimeMillis();
        this.serviceMethod = serviceMethod;
    }

    public void printPerformance() {
        this.end = System.currentTimeMillis();
        long elapse = end - begin;

        System.err.println(serviceMethod + "共花费" + elapse + "毫秒");
    }

    public long getBegin() {
        return begin;
    }

    public void setBegin(long begin) {
        this.begin = begin;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public String getServiceMethod() {
        return serviceMethod;
    }

    public void setServiceMethod(String serviceMethod) {
        this.serviceMethod = serviceMethod;
    }
}
