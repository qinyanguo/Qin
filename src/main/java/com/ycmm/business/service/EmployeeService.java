package com.ycmm.business.service;

import com.ycmm.base.bean.BizParamBean;
import com.ycmm.base.bean.ResultBean;
import com.ycmm.base.system.UserLoginCheck;
import com.ycmm.model.BaseBizEmployee;

import java.util.List;
import java.util.Map;


/**
 * 业务员 业务接口类
 */
public interface EmployeeService {


    public ResultBean create(BizParamBean bizParamBean) throws Exception;

	/**
	 * 查询业务员列表
	 */
	@UserLoginCheck
	public ResultBean queryEmployeeList(BizParamBean bizParamBean) throws Exception;

    public List<BaseBizEmployee> queryEmployeeList(BaseBizEmployee baseBizEmployee) throws Exception;

    public List<Map<String, Object>> queryEmployeeExcel(BaseBizEmployee baseBizEmployee) throws Exception;

	/**
	 * 根据 业务员 id 查询业务员 信息
	 */
	@UserLoginCheck
	public ResultBean queryEmployeeInfo(BizParamBean bizParamBean) throws Exception;

	/**
	 * 根据 业务员 id 查询 业务员信息
	 */
	public BaseBizEmployee queryEmployeeInfo(Integer employeeId) throws Exception;

	/**
	 * 根据业务员姓名查询业务员信息
	 */
	public ResultBean queryByName(BizParamBean bizParamBean) throws Exception;


	/**
	 * 更新业务员详情
	 */
	public ResultBean updateEmployeeInfo(BizParamBean bizParamBean) throws Exception;
	/**
	 * 修改业务员密码
	 */
	public ResultBean updatePassword(BizParamBean bizParamBean) throws Exception;

    public int update(BaseBizEmployee employee) throws Exception;




}
