package com.ycmm.business.service;


import com.ycmm.base.bean.BizParamBean;
import com.ycmm.base.bean.ResultBean;
import com.ycmm.base.system.UserLoginCheck;
import com.ycmm.model.BizUser;

/**
 * 用户 业务处理接口
 */
@UserLoginCheck
public interface UserService {

    /**
     * 用户注册接口
     */
    public ResultBean registerUser(BizParamBean bizParamBean) throws Exception;

	/**
	 * 用户修改密码
	 */
	public ResultBean updateUserPassword(BizParamBean bizParamBean) throws Exception;

	/**
	 * 获取 用户 信息
	 */
	public ResultBean queryUserInfo(BizParamBean bizParamBean) throws Exception;

    /**
	 * 移除用户缓存信息
	 */
	public void removeCacheUser(String uid) throws Exception;

	/**
	 * 根据 uid 查询用户信息
	 */
	public BizUser queryUserInfo(String uid) throws Exception;

	/**
	 * 根据 电话 查询用户信息
	 */
	public BizUser queryUserInfoByPhone(String phone) throws Exception;

	/**
	 * 根据 注册用户里面的客户id查询用户信息
	 */
	public BizUser queryUserInfoByCustomerId(int customerId) throws Exception;

	/**
	 * 更新 用户基本资料
	 */
	public ResultBean updateUserInfo(BizParamBean bizParamBean) throws Exception;

	/**
	 * 获取 业务员信息
	 */
	public ResultBean queryEmployeeInfo(BizParamBean bizParamBean) throws Exception;


	/**
	 * 获取用户列表信息
	 */
	public ResultBean queryUserList(BizParamBean bizParamBean) throws Exception;

	public ResultBean testRedis() throws Exception;

}
