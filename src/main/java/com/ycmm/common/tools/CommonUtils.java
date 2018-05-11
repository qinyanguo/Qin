package com.ycmm.common.tools;

import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by jishubu on 2018/5/11.
 */
public class CommonUtils {

    /**
     * 获取浏览器类型
     * @param request
     * @return
     */
    public static String getModel(HttpServletRequest request) {

        String header = request.getHeader("User-Agent");
        if(StringUtils.isEmpty(header)) {
            return "UNKNOWN";
        }
        if(header.toUpperCase().contains("MICROMESSENGER")) {
            return "WECHART";
        }
        UserAgent userAgent = UserAgent.parseUserAgentString(header);
        OperatingSystem operatingSystem = userAgent.getOperatingSystem();
        String webName = operatingSystem.getName().toUpperCase();
        if (webName.contains("ANDROID")) {
            return "ANDROID";
        }else if (webName.contains("IPHONE")) {
            return "IOS";
        }else if (webName.contains("WINDOWS")) {
            return "PC";
        }
        return "UNKNOWN";
    }
}
