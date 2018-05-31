package com.ycmm.common.interceptor;

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

    @Before("within(@org.springframework.stereotype.Controller *) && @annotation(requestLimit)")
    public void requestLimit(final JoinPoint joinPoint, RequestLimit requestLimit) throws Exception{
        Object[] args = joinPoint.getArgs();
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
        String ip = WebUtils.getNginxAddress(request);
        String url = request.getRequestURL().toString();
        String key = "req_limit".concat(url).concat(ip);
        Long count = redisCache.incr(key);
        if (count <= 1) {
            redisCache.set(key, redisCache.get(key), requestLimit.time());
        }
        if (count > requestLimit.count()) {
            logger.info("用户IP["+ ip + "]访问地址[" + url +"]超过了限制次数["+ requestLimit.count() +"]");
//            发通知提醒
//            throw new ErrorMsgException("请求频率过快，请等待"+ requestLimit.time()/1000 +"秒再访问。");
            String aggressKey = "attack_limit".concat(url).concat(ip);
            Long attackCount = redisCache.incr(aggressKey);
            if (attackCount <= 1) {
                redisCache.set(aggressKey, redisCache.get(aggressKey), requestLimit.time() * 30);
            }
            //假如一分钟(requestLimit.time)内只允许请求 5 次，每当一分钟内访问次数大于 5 时记攻击次数加 1，
            // 即使每分钟访问次数都大于 5，在 （requestLimit.time() * 30）30 分钟内最大攻击次数也就为 30，
            // 所以如果 30 分钟内大于 20 次，意为恶意攻击，将其 ip 可拉黑一段时间
            if (attackCount > (requestLimit.count() * 4)) {
                //拉黑ip
            }
        }else {
            //可以调用逻辑
        }

    }
}
