package com.ycmm.front.handle.service;

import com.ycmm.base.FrontParamBean;
import com.ycmm.base.ResultBean;


public interface FrontHandleService {

    public ResultBean frontHandle(FrontParamBean frontParamBean, String ip, String model) throws Exception;

}