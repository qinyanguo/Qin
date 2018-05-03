package com.ycmm.system;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 必须配置在接口类 Front 层校验
 * 也可继续配置在实现类上 才可双重校验
 * 用户登录校验处理  默认 ：类    > 方法
 * @author jishubu
 *
 */
@Inherited
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UserLoginCheck {

}
