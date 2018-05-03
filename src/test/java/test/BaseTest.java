package test;

import org.junit.Before;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by jishubu on 2018/5/2.
 */
public class BaseTest {

    private static ClassPathXmlApplicationContext context;

    @Before
    public void before() {
        if (context == null || !context.isRunning()){
            context = new ClassPathXmlApplicationContext("/applicationContext.xml");
        }
        if (!context.isRunning()) {
            context.start();
        }
        //模拟登录
        String url = "";
    }


    //针对不能获取bean，测试是否能获取bean，判断是配置文件问题还是注解问题
    public static void main(String[] args) {
        context = new ClassPathXmlApplicationContext("/applicationContext.xml");
        Object stockMapper = context.getBean("StockMapper");
    }

}
