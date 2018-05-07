package com.ycmm.front.handle.service.impl;

import com.ycmm.base.bean.BizParamBean;
import com.ycmm.base.bean.FrontParamBean;
import com.ycmm.base.bean.ResultBean;
import com.ycmm.base.exceptions.base.BadRequestException;
import com.ycmm.base.exceptions.enums.ErrorMsgEnum;
import com.ycmm.base.exceptions.base.ErrorMsgException;
import com.ycmm.front.handle.service.FrontHandleService;
import com.ycmm.front.user.common.UserSignCheck;
import com.ycmm.common.constants.Constants;
import com.ycmm.base.system.RemoveLoginCheck;
import com.ycmm.base.system.UserLoginCheck;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;

/**
 * Created by jishubu on 2018/4/23.
 */
@Service
public class FrontHandleServiceImpl implements FrontHandleService {

    private static Logger logger = Logger.getLogger(FrontHandleServiceImpl.class);

    @Override
    public ResultBean frontHandle(FrontParamBean frontParam, String ip, String model) throws Exception {
        ResultBean result = null;
        try {
            // 验证码攻击处理
            if ("ANDROID".equals(model)) {
                if ("getVerifyCode".equals(frontParam.getBiz_method())
                        || "getRePwdCode".equals(frontParam.getBiz_method())
                        || "getLoginCode".equals(frontParam.getBiz_method())
                        || "getBindingCode".equals(frontParam.getBiz_method())) {
                    // 判断是否是攻击的设备唯一标志
                    if ("13065ffa4e344b5334e".equals(frontParam.getDev())) {
                        logger.error("===========验证码攻击拦截==========[" + ip + "]");
                        return new ResultBean("验证码获取成功");
                    }
                }
            }

            // 自定义 业务接口对象名 根据变量名XXX获得字段     获得frontParam对象所属的类型的对象
            Field field = this.getClass().getDeclaredField(frontParam.getBiz_module());
            // 设置字段可访问，即暴力反射
            field.setAccessible(true);
            // 在那个对象上获取此字段的值
            Object object = field.get(this);
            // 返回一个 Class 对象，它标识了此 Field 对象所表示字段的声明类型。 返回：标识此对象所表示字段的声明类型的Class对象
            Class<?> clazz = field.getType();
            // 调取 业务接口方法
            /**
             * getMethod方法第一个参数指定一个需要调用的方法名称
             * 第二个参数是需要调用方法的参数类型列表，是参数类型！如无参数可以指定null　该方法返回一个方法对象
             * name - 方法名                 parameterTypes - 参数列表
             * 返回： 与指定的 name 和 parameterTypes匹配的 Method 对象，它反映此 Class 对象所表示的类或接口的指定公共成员方法
             */
            Method method = clazz.getMethod(frontParam.getBiz_method(), new Class[] { BizParamBean.class }); 	//参数必须和方法中一样int和Integer，double和Double被视为不同的类型
            //获取方法上的注解
            RemoveLoginCheck removeLoginCheck = method.getAnnotation(RemoveLoginCheck.class);
            UserLoginCheck annotation = null;
            if (removeLoginCheck == null) {
                annotation = clazz.getAnnotation(UserLoginCheck.class);
                if (annotation == null) {
                    annotation = method.getAnnotation(UserLoginCheck.class);
                }
            }
            if(annotation != null) {
                UserSignCheck.isSigncheck(frontParam);
            } else {
                JSONObject token = UserSignCheck.getAccountToken(frontParam.getSID());
                if(token != null) {
                    frontParam.setUid(token.optString("UID"));
                } else {
                    frontParam.setUid(null);
                    frontParam.setSID(null);
                }
            }
            BizParamBean bizParamBean = new BizParamBean(frontParam);
            bizParamBean.setIp(ip);
            bizParamBean.setModel(model);
            // 第一个参数为隐式参数该方法不是静态方法必须指定
            result = (ResultBean) method.invoke(object, new Object[]{BizParamBean.class});
            //执行日志
            String logInfo = MessageFormat.format(Constants.LOGGER_INFO_FORMAT, bizParamBean.toString(), frontParam.toString(),
                    bizParamBean.getBiz_param().toString(), result.getMsg());
            logger.info(logInfo);
        } catch (Exception e) {
            if(e instanceof NullPointerException) {
                e = new BadRequestException();
            }
            if(e instanceof NoSuchMethodException) {
                e = new ErrorMsgException(ErrorMsgEnum.ERROR_SERVER, "暂未提供[" + frontParam.getBiz_method() + "]服务");
            }
            if(e instanceof NoSuchFieldException) {  //类不包含指定名称的字段时产生的信号。
                e = new ErrorMsgException(ErrorMsgEnum.ERROR_SERVER, "暂未提供[" + frontParam.getBiz_module() + "]服务");
            }
            /**
             * InvocationTargetException 是一种包装由调用方法或构造方法所抛出异常的受查异常。
             * 这个异常并不是Eclipse插件开发特有的，而是标准JDK中的，它定义在 java.lang.reflect包下。
             * 在进行Java开发的时候很少会接触到这个异常，不过在进行Eclipse插件开发中则不同，很多API都声明抛出此类异常，因此必须对此异常进行处理。
             *
             * 由InvocationTargetException的源码得知InvocationTargetException 并没有覆盖getMessage方法，
             * 所以如果直接抛出e或e.getMessage()消息就会为空。我们需要调用InvocationTargetException 的getTargetException方法得到要被包装的异常，
             * 这个异常才是真正我们需要的异常
             */
            if(e instanceof InvocationTargetException) {
                Throwable targetException = ((InvocationTargetException) e).getTargetException();
                e = (Exception)targetException;
            }
            throw e;
        }
        /**
         * Method method = ownerClass.getMethod(methodName, argsClass)：
         * 通过methodName和参数的argsClass（方法中的参数类型集合）数组得到要执行的Method。
         *
         * method.invoke(owner, args)：执行该Method.invoke方法的参数是执行这个方法的对象owner，
         * 和参数数组args，可以这么理解：owner对象中带有参数args的method方法。返回值是Object，也既是该方法的返回值。
         */
        return result;
    }
}