package com.ycmm.common.designpattern.proxy.jdk;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Author Oliver.qin
 * @Date 2018/8/2 17:43
 * @Description:
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/META-INF/applicationContext.xml")
public class JdkTest {

    @Autowired
    PerformanceHandlerProxy performanceHandler;

    @Test
    public void testSeller() {
        SellerService proxySellerService = this.performanceHandler.getProxyObject();
        System.out.println(proxySellerService.getClass());
        proxySellerService.sellTicket(100);
    }
}
