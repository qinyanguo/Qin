package test;

import com.ycmm.business.service.EmployeeService;
import org.junit.After;
import org.junit.Before;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by jishubu on 2018/5/2.
 */
public class BaseTest {

    public static ClassPathXmlApplicationContext context;

    @Before
    public void setBefore() {
        if (context == null || !context.isRunning()){
            context = new ClassPathXmlApplicationContext("/META-INF/applicationContext.xml");
        }
        if (!context.isRunning()) {
            context.start();
        }
        //模拟登录
        String url = "";
        System.out.println("==========@Before===========");
    }

    @After
    public void setAfter() {
        if (context != null && context.isRunning()) {
            context.stop();
        }
    }

    public <T> T getBean(Class<T> clazz) {
        return  context.getBean(clazz);
    }

    //针对不能获取bean，测试是否能获取bean，判断是配置文件问题还是注解问题
    public static void main(String[] args) {
        context = new ClassPathXmlApplicationContext("/META-INF/applicationContext" +
                ".xml");
//        Object stock = context.getBean("baseBizEmployeeMapper");   //""开头字母小写
        Object stock = context.getBean(EmployeeService.class);   //""开头字母小写
        System.out.println(stock);
    }

}
