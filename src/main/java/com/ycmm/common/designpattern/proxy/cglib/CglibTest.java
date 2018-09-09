package com.ycmm.common.designpattern.proxy.cglib;

import com.ycmm.common.designpattern.proxy.jdk.PerformanceHandlerProxy;
import com.ycmm.common.designpattern.proxy.jdk.SellerService;
import com.ycmm.common.designpattern.proxy.jdk.SellerServiceImpl;
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
public class CglibTest {

    @Autowired
    CglibHandlerProxy cglibHandlerProxy;

    @Test
    public void testSeller() {
        Seller proxySeller = cglibHandlerProxy.getCglibProxy(Seller.class);
//        System.out.println(proxySeller.getClass());
        proxySeller.sellTicket(100);
    }
}
