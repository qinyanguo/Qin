package com.ycmm.base.bean;

import java.io.Serializable;

import com.ycmm.base.bean.FrontParamBean;
import net.sf.json.JSONObject;

/**
 * 前端请求参数封装
 *
 * @author jishubu
 */
public class BizParamBean implements Serializable, Cloneable {

    private static final long serialVersionUID = 6757046488868484897L;

    /**
     * 请求版本号
     */
    private String version;
    /**
     * 请求客户端  模型  类型
     */
    private String model;
    /**
     * 用户session id
     */
    private String SID;
    /**
     * 用户请求ip地址
     */
    private String ip;
    /**
     * 用户uid
     */
    private String uid;
    /**
     * 业务请求参数
     */
    private JSONObject biz_param;

    public BizParamBean(FrontParamBean frontParamBean) {
        this.SID = frontParamBean.getSID();
        this.uid = frontParamBean.getUid();
        this.biz_param = frontParamBean.getBiz_param();
        this.version = frontParamBean.getVersion();
    }


    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSID() {
        return SID;
    }

    public void setSID(String sID) {
        SID = sID;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public JSONObject getBiz_param() {
        return biz_param;
    }

    public void setBiz_param(JSONObject biz_param) {
        this.biz_param = biz_param;
    }


}





























