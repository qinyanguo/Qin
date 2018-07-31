package com.ycmm.common.interceptor;

import net.sf.json.JSONObject;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

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
@Component
public class InOutCollectionUtilAspect {

    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    private void requestMappingMethod() {

    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.ResponseBody)")
    private void responseBodyMethod() {

    }

    /**
     * Before
     * 在核心业务执行前执行，不能阻止核心业务的调用。
     * @param joinPoint
     */
    @Before(value = "requestMappingMethod() && responseBodyMethod()")
    public JSONObject beforeCollection(JoinPoint joinPoint) {
        System.out.println("--------beforeCollection().invoke--------");
        System.out.println("--------此处可以对返回值做进一步处理--------");
        System.out.println("--------可通过joinPoint来获取所需要的内容--------");
        System.out.println("--------End of beforeCollection--------");
        return null;
    }


    /**
     * Around
     * 手动控制调用核心业务逻辑，以及调用前和调用后的处理,
     *
     * 注意：当核心业务抛异常后，立即退出，转向AfterAdvice
     * 执行完AfterAdvice，再转到ThrowingAdvice
     * @param pjp
     * @return
     * @throws Throwable
     */
//    @Around(value = "requestMappingMethod() && responseBodyMethod()")
//    public Object aroundAdvice(ProceedingJoinPoint pjp) throws Throwable {
//        System.out.println("-----aroundAdvice().invoke-----");
//        System.out.println(" 此处可以做类似于Before Advice的事情");
//
//        //调用核心逻辑
//        Object retVal = pjp.proceed();
//        System.out.println(" 此处可以做类似于After Advice的事情");
//        System.out.println("-----End of aroundAdvice()------");
//        return retVal;
//    }

    /**
     * AfterReturning
     * 核心业务逻辑调用正常退出后，不管是否有返回值，正常退出后，均执行此Advice
     */
    @AfterReturning(value = "requestMappingMethod() && responseBodyMethod()", returning = "response")
    public JSONObject afterReturningCollection(JoinPoint joinPoint, HttpServletResponse response) {
        System.out.println("--------afterReturningCollection().invoke--------");
        System.out.println("Return value: -------->" + response);
        System.out.println("--------此处可以对返回值做进一步处理--------");
        System.out.println("--------可通过joinPoint来获取所需要的内容--------");
        System.out.println("--------End of afterReturningCollection--------");
        return null;
    }

    /**
     * 核心业务逻辑调用异常退出后，执行此Advice，处理错误信息
     *
     * 注意：执行顺序在Around Advice之后
     */
    @AfterThrowing(value = "requestMappingMethod() && responseBodyMethod()", throwing = "ex")
    public void afterThrowingCollection(JoinPoint joinPoint, Exception ex) {
        System.out.println("--------afterThrowingCollection().invoke--------");
        System.out.println("错误信息: -------->" + ex.getMessage());
    }
}
