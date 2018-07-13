package com.ycmm.common.interceptor;

import com.ycmm.base.bean.FrontParamBean;
import com.ycmm.base.exceptions.base.ErrorMsgException;
import com.ycmm.base.system.RequestLimit;
import com.ycmm.common.cache.CacheService;
import com.ycmm.common.cache.impl.RedisCacheImpl;
import com.ycmm.common.utils.WebUtils;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 *当有多个切面时，Order决定先执行哪个后执行哪个。如果不加Order则默认按照注解标注的先后顺序执行。
 * @Order(Ordered.HIGHEST_PRECEDENCE)表示最高优先级。
 * 比如 @Order(1)  @Order(2)
 */
@Aspect
@Component
public class RequestLimitAspect {

    private static final Logger logger = Logger.getLogger(RequestLimit.class);

    @Autowired
    @Qualifier("redisCache")
    private CacheService redisCache;

    /**
     * @within：用于匹配所以持有指定注解类型内的方法；@annotation(limit) 是匹配含有limit注解的方法。
     *
     * @Before("within(@org.springframework.stereotype.Controller *) && @annotation(limit)")
     *                  表示对含有SpringMVC的Controller注解下面的方法 且含有 注解limit的方法有效。
     * jointPoint获取HttpServletRequest（被修饰的方法需要有HttpServletRequest参数）
     * redis/Jedis不熟悉的只需要了解下两个方法就行:incr方法表示为key值加1，如果key不存在则新建一个key值，
     * 并初始化为1。expire表示经过time时间后key会消失。
     * 逻辑是某个Ip地址对某个接口url,在过期时间内如果超过规定的次数就会抛出访问频率过高的异常。
     */

    @Before("within(@org.springframework.stereotype.Controller *) && @annotation(requestLimit)")
    public void requestLimit(final JoinPoint joinPoint, RequestLimit requestLimit) throws Exception{

        //得到方法名
        System.out.println(joinPoint.getSignature().getName());
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof FrontParamBean) {
                String uid = ((FrontParamBean) arg).getUid();
                System.out.println(uid);
            }
        }
        HttpServletRequest request = null;
        for (int i = 0; i < args.length; i++) {
            Object o =  args[i];
                if (o instanceof HttpServletRequest) {
                request = (HttpServletRequest) o;
                break;
            }
        }
        if (request == null) {
            return;
        }

        //判断当前请求的用户是否在拉黑名单里

//        redisCache.getValue("");

        String ip = WebUtils.getNginxAddress(request);
        String url = request.getRequestURL().toString();
//        String key = "表示正常访问的数字".concat("用户id").concat(ip.replace(".", ""));
        String key = "111".concat("231").concat(ip.replace(".", ""));;

        //incr的key存储的字符串类型不能表示为一个整数，那么执行这个命令时服务器会返回一个错误(eq:(error) ERR value is not an integer or out of range)。
        //Long count = redisCache.incr(key);  此法报错
        Long count =  redisCache.setIncr(key, "1", 0,requestLimit.time(), 1);
        if (count < 1) {
            count = redisCache.setIncr(key,  "1", 0, requestLimit.time(), 1);
        }

        if (count > requestLimit.count()) {
            logger.info("用户IP["+ ip + "]访问地址[" + url +"]超过了限制次数["+ requestLimit.count() +"]");
//            发通知提醒
            System.err.println("提醒------->" + redisCache.getValue(key) + "次");
//            throw new ErrorMsgException("请求频率过快，请等待"+ requestLimit.time()/1000 +"秒再访问。");
//            String aggressKey = "表示正常访问的数字".concat("用户id").concat(ip.replace(".", ""));
            String aggressKey = "222".concat("231").concat(ip.replace(".", ""));
            Long attackCount =  redisCache.setIncr(aggressKey, "1", 0, requestLimit.time(), 2);
            if (attackCount < 1) {
                attackCount =  redisCache.setIncr(aggressKey, "1", 0, requestLimit.time(), 2);
            }
            //假如一分钟(requestLimit.time)内只允许请求 5 次，每当一分钟内访问次数大于 5 时记攻击次数加 1，
            // 即使每分钟访问次数都大于 5，在 （requestLimit.time() * 30）30 分钟内最大攻击次数也就为 30，
            // 所以如果 30 分钟内大于 20 次，意为恶意攻击，将其 ip 可拉黑一段时间
            if (attackCount > (requestLimit.count() * 4)) {
                //拉黑ip
                System.err.println("拉黑了，哈哈哈哈哈哈");
            }
        }else {
            //可以调用逻辑
            System.out.println("=======可以调用逻辑=====");
        }

    }
}
