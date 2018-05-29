package com.ycmm.base.system;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.lang.annotation.*;

/**
 * 允许访问的次数限制注解
 */
@Documented
@Order(Ordered.HIGHEST_PRECEDENCE)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE})
public @interface RequestLimit {

    /**
     * 指定时间内允许访问的最大次数
     */
    int count() default 5;

    /**
     * 以第一次请求时间向后推
     * 例如第一次请求的时间为18:00:00,请求时间设置为一分钟，在18:01:00时间段内访问次数超过count就会报错或提示，
     * 但是到18:01:00之后就会重新开始计数，而不是以最后访问时间为准，
     * 时间段，单位为毫秒，默认为 1分钟
     * 如果是防攻击，建议把 time设置大一点例如 1分钟20次
     * 如果想避免重复提交问题，建议 time设置小一点如 1秒1次
     *
     */
    int time() default 60000;

    /**
     * 比如，每5分钟超过规定访问次数 就让 totalCount加 1，若totalCount 超过一定次数  说明是有意攻击，从而将ip拉黑
     * @return
     */
    int totalCount() default 0;
}
