package com.ycmm.common.interceptor;

import net.sf.json.JSONObject;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * @Aspect修饰类，说明该类是切面关注的抽象，即针对切面目标对象要做什么操作。
 *
 * @Pointcut针对Spring MVC的RequestMapping和ResponseBody注解（@Iterface)。
 *
 * @Before、@After、@AfterReturing等针对目标对象相应动作(before、after等)做出相应的操作。
 *
 * JoinPoint joint 可以获取目标对象的参数。Object[] argcs= joinPoint.getArgs(); argcs类似于main方法的参数数组，
 * 只不过这里需要类型转换把Object转换成相应的type。
 */
@Aspect
public class InOutCollectionUtilAspect {

    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    private void requestMappingMethod() {

    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestBody)")
    private void responseBodyMethod() {

    }

    @Before(value = "requestMappingMethod() && responseBodyMethod()")
    public JSONObject beforeCollection(JoinPoint joinPoint) {
        System.out.println("--------beforeCollection().invoke--------");
        System.out.println("--------此处可以对返回值做进一步处理--------");
        System.out.println("--------可通过joinPoint来获取所需要的内容--------");
        System.out.println("--------End of beforeCollection--------");
        return null;
    }

    @AfterReturning(value = "requestMappingMethod() && responseBodyMethod()", returning = "response")
    public JSONObject afterReturningCollection(JoinPoint joinPoint, HttpServletResponse response) {
        System.out.println("--------afterReturningCollection().invoke--------");
        System.out.println("Return value: -------->" + response);
        System.out.println("--------此处可以对返回值做进一步处理--------");
        System.out.println("--------可通过joinPoint来获取所需要的内容--------");
        System.out.println("--------End of afterReturningCollection--------");
        return null;
    }

    @AfterThrowing(value = "requestMappingMethod() && responseBodyMethod()", throwing = "ex")
    public void afterThrowingCollection(JoinPoint joinPoint, Exception ex) {
        System.out.println("--------afterThrowingCollection().invoke--------");
        System.out.println("错误信息: -------->" + ex.getMessage());
    }
}
