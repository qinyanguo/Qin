package com.ycmm.business.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.ycmm.base.bean.Page;
import com.ycmm.base.enums.Model;
import com.ycmm.base.exceptions.base.ErrorMsgException;
import com.ycmm.business.mapper.BaseBizEmployeeMapper;
import com.ycmm.business.mapper.BizUserMapper;
import com.ycmm.business.service.EmployeeService;
import com.ycmm.business.service.UserService;
import com.ycmm.common.cache.CacheService;
import com.ycmm.common.constants.Constants;
import com.ycmm.common.lock.Lock;
import com.ycmm.common.lock.impl.RedisLock;
import com.ycmm.common.utils.DateUtils;
import com.ycmm.common.utils.IdKit;
import com.ycmm.common.utils.NameCheckOut;
import com.ycmm.model.BaseBizEmployee;
import com.ycmm.model.BizUser;
import com.ycmm.model.BizUserExample;
import net.sf.json.JSONObject;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ycmm.base.bean.BizParamBean;
import com.ycmm.base.bean.ResultBean;
import com.ycmm.base.system.UserLoginCheck;

@Service
@UserLoginCheck
public class UserServiceImpl implements UserService {

    @Autowired
    private BizUserMapper bizUserMapper;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private BaseBizEmployeeMapper baseBizEmployeeMapper;

    @Autowired
    @Qualifier("redisCache")
    private CacheService redisCache;

    @Override
    public ResultBean registerUser(BizParamBean bizParamBean) throws Exception {
        BizUser bizuser = bizParamBean.getBiz_param(BizUser.class);
        if (StringUtils.isNotBlank(bizuser.getFullname())) {
            if (!NameCheckOut.isCheckNameNorm(bizuser.getFullname())) {
                throw new ErrorMsgException("请检查您输入的名称,可能包含非法字符哟");
            }
            if (!NameCheckOut.isCheckName(bizuser.getFullname())) {
                throw new ErrorMsgException("请检查您输入的名称长度(中文不能超过15个字,英文不要超过30个字符哟)");
            }
            if (!NameCheckOut.isCheckNameEspecial(bizuser.getFullname())) {
                throw new ErrorMsgException("亲,该名称已被占用了,请换个名字试试哟！");
            }
            bizuser.setFullname(bizuser.getFullname().replaceAll("\\s*", ""));
        }
        if (StringUtils.isEmpty(bizuser.getPhone())) {
            throw new ErrorMsgException("请输入注册手机号码");
        }

        if (StringUtils.isEmpty(bizuser.getPassword())) {
            throw new ErrorMsgException("请输入密码");
        }
        if (bizuser.getPassword().length() > 16) {
            throw new ErrorMsgException("密码长度不能超过16位！");
        }
        if (StringUtils.isEmpty(bizuser.getCode())) {
            throw new ErrorMsgException("请输入有效的验证码");
        }
        Integer code = redisCache.get(bizuser.getPhone() + Constants.REGISTER_VERVIFY_CODE, Integer.class);
        if (code == null) {
            throw new ErrorMsgException("验证码失效，请重新获取");
        }
        if (!String.valueOf(code).equals(bizuser.getCode())) {
            throw new ErrorMsgException("验证码错误，请重新尝试");
        }
        bizuser.setId(IdKit.getUniversalId());

        bizuser.setSource(StringUtils.isEmpty(bizParamBean.getModel()) ? 0 : Model.valueOf(bizParamBean.getModel()).getId());
        String md5Hex = DigestUtils.md5Hex(DigestUtils.shaHex(bizuser.getPassword()));
        bizuser.setPassword(md5Hex);
        String syn = Constants.USER_REGISTER_SYN + bizuser.getPhone();
        Lock lock = new RedisLock(syn, 8000L);
        try {
            lock.tryLock(8, TimeUnit.SECONDS);
            // 再次校验该注册用户是否已经是会员
            BizUserExample example = new BizUserExample();
            example.createCriteria().andPhoneEqualTo(bizuser.getPhone());
            List<BizUser> list = bizUserMapper.selectByExample(example);
            if (!list.isEmpty()) {
                throw new ErrorMsgException("此用户已注册会员，可直接登录");
            }
            if (bizUserMapper.insertSelective(bizuser) > 0) {
                redisCache.remove(bizuser.getPhone() + Constants.REGISTER_VERVIFY_CODE);
                return new ResultBean("用户注册成功!");
            }
        } finally {
            lock.unLock();
        }
        throw new ErrorMsgException("注册失败,请稍后重试!");
    }

    @Override
    public ResultBean queryUserInfo(BizParamBean bizParamBean) throws Exception {
        BizUser bizUser = queryUserInfo(bizParamBean.getBiz_param().optString("id"));
        bizUser.setSecretKey(null);
        bizUser.setPassword(null);
        return new ResultBean(bizUser);
    }

    @Override
    public void removeCacheUser(String uid) throws Exception {
        redisCache.remove(uid);
    }

    @Override
    public BizUser queryUserInfo(String uid) throws Exception {
        BizUser bizUser = redisCache.get(uid, BizUser.class);
        if (bizUser == null) {
            bizUser = bizUserMapper.selectByPrimaryKey(uid);
            if (bizUser == null) {
                throw new ErrorMsgException("此注册用户不存在，请检查后重试");
            }
            if (bizUser.getStatus() != 1) {
                throw new ErrorMsgException("此帐号已被禁用，请联系客服处理");
            }
            BaseBizEmployee employee = employeeService.queryEmployeeInfo(bizUser.getEmployee());
            redisCache.set(uid, bizUser, 2 * 24 * 60 * 60);
        }
        return bizUser;
    }

    @Override
    public BizUser queryUserInfoByPhone(String phone) throws Exception {
        BizUserExample example = new BizUserExample();
        BizUserExample.Criteria criteria = example.createCriteria();
        criteria.andPhoneEqualTo(phone);
        List<BizUser> bizUsers = bizUserMapper.selectByExample(example);
        if (bizUsers.isEmpty()) {
            return null;
        }
        if (bizUsers.size() > 1) {
            throw new ErrorMsgException("电话：" + phone + "的注册客户存在多个，数据有问题");
        }
        return bizUsers.get(0);
    }

    @Override
    public BizUser queryUserInfoByCustomerId(int customerId) throws Exception {
        BizUserExample example = new BizUserExample();
        BizUserExample.Criteria criteria = example.createCriteria();
        criteria.andCustomerIdEqualTo(customerId);
        List<BizUser> bizUsers = bizUserMapper.selectByExample(example);
        if (bizUsers.isEmpty()) {
            return null;
        }
        if (bizUsers.size() > 1) {
            throw new ErrorMsgException("该注册客户存在多个，数据有问题");
        }
        return bizUsers.get(0);
    }

    @Override
    public ResultBean updateUserInfo(BizParamBean bizParamBean) throws Exception {
        BizUser oldUser = queryUserInfo(bizParamBean.getUid());
        BizUser bizuser = bizParamBean.getBiz_param(BizUser.class);
        bizuser.setId(bizParamBean.getUid());
        if (StringUtils.isNotBlank(bizuser.getBizMain())) {
            String[] split = bizuser.getBizMain().split(",");
            if (split.length > 80) {
                throw new ErrorMsgException("输入的主营品类不能超过80个哟");
            }

        }
        bizuser.setBizType(oldUser.getManageType());
        if (StringUtils.isBlank(bizuser.getFullname()) && StringUtils.isBlank(oldUser.getFullname()) && bizuser.getManageType() == null
                && oldUser.getManageType() == null && StringUtils.isBlank(bizuser.getBizMain())
                && StringUtils.isBlank(oldUser.getBizMain())) {
            throw new ErrorMsgException("请完善个人信息");
        }
        String uTime = DateUtils.getDateToString(System.currentTimeMillis());
        bizuser.setUtype(null);
        bizuser.setPhone(null);// 置空选项
        bizuser.setSource(null);
        bizuser.setCtype(null);
        bizuser.setCustomerId(null);
        // bizuser.setEmployee(null);
        bizuser.setStatus(null);
        bizuser.setTransStatus(null);
        bizuser.setImportance(null);
        bizuser.setScore(null);
        bizuser.setGrade(null);
        bizuser.setOpenId(null);
        bizuser.setSecretKey(null);
        bizuser.setUtime(uTime);
        if (bizUserMapper.updateByPrimaryKeySelective(bizuser) >= 0) {
            return new ResultBean("更新信息成功!");
        }
        throw new ErrorMsgException("信息更新失败!");

    }

    @Override
    public ResultBean updateUserPassword(BizParamBean bizParamBean) throws Exception {
        BizUser bizuser = bizParamBean.getBiz_param(BizUser.class);
        if (StringUtils.isBlank(bizuser.getPassword())) {
            throw new ErrorMsgException("请输入原密码！");
        }
        if (StringUtils.isBlank(bizuser.getNewPassword())) {
            throw new ErrorMsgException("请输入新密码！");
        }
        if (bizuser.getNewPassword().length() > 16) {
            throw new ErrorMsgException("密码长度不能超过16位！");
        }
        if (bizuser.getPassword().equals(bizuser.getNewPassword())) {
            throw new ErrorMsgException("新密码不可与原密码一致！");
        }
        BizUser bizUser = bizUserMapper.selectByPrimaryKey(bizParamBean.getUid());
        String md5Hex = DigestUtils.md5Hex(DigestUtils.shaHex(bizuser.getPassword()));
        if (!bizUser.getPassword().equals(md5Hex)) {
            throw new ErrorMsgException("原密码错误，请重试！");
        }
        bizuser.setId(bizParamBean.getUid());
        String password = DigestUtils.md5Hex(DigestUtils.shaHex(bizuser.getNewPassword()));
        bizuser.setPassword(password);
        if (bizUserMapper.updateByPrimaryKeySelective(bizuser) > 0) {
            return new ResultBean("密码更新成功!");
        }
        throw new ErrorMsgException("密码更新失败!");
    }

    @Override
    public ResultBean queryEmployeeInfo(BizParamBean bizParamBean) throws Exception {
        BizUser bizUser = queryUserInfo(bizParamBean.getUid());
        BaseBizEmployee employee = employeeService.queryEmployeeInfo(bizUser.getEmployee());
        if ("IOS".equals(bizParamBean.getModel()) && "3.5.9".equals(bizParamBean.getVersion())) {
            return new ResultBean(employee);
        }
        if (employee == null) {
            // throw new ErrorMsgException("暂无该业务员详细信息");
            return new ResultBean(new JSONObject());
        }
        JSONObject json = new JSONObject();
        json.put("name", employee.getName());
        json.put("gender", employee.getGender());// 性别
        json.put("photo", employee.getPhoto());// 头像
        json.put("extno", employee.getExtno());// 分机号
        json.put("goodfield", employee.getGoodfield());// 擅长领域
        return new ResultBean(json);
    }


    @Override
    public ResultBean queryUserList(BizParamBean bizParamBean) throws Exception {
        BizUserExample example = new BizUserExample();
        com.ycmm.model.BizUserExample.Criteria createCriteria = example.createCriteria();
        JSONObject biz_param = bizParamBean.getBiz_param();
        String id = biz_param.optString("id").trim();
        String phone = biz_param.optString("phone").trim();
        String fullname = biz_param.optString("fullname").trim();
        Page page = (Page) JSONObject.toBean(biz_param, Page.class);
        if (StringUtils.isNotBlank(id)) {
            createCriteria.andIdEqualTo(id);
        }
        if (StringUtils.isNotEmpty(phone)) {
            createCriteria.andPhoneLike(phone + "%");
        }
        if (StringUtils.isNotEmpty(fullname)) {
            createCriteria.andFullnameLike("%" + fullname + "%");
        }
        example.setStartIndex(page.getStartIndex());
        example.setPageSize(page.getpSize());
        example.setOrderByClause(" ctime desc ");
        List<BizUser> list = bizUserMapper.selectByExample(example);
        List<JSONObject> result = new ArrayList<JSONObject>();
        for (BizUser user : list) {
            JSONObject json = new JSONObject();
            json.put("id", user.getId());
            StringBuilder str = new StringBuilder(user.getPhone());
            str.setCharAt(3, '*');
            str.setCharAt(4, '*');
            str.setCharAt(5, '*');
            str.setCharAt(6, '*');
            json.put("phone", str.toString());
            json.put("type", user.getType());
            json.put("gender", user.getGender());
            json.put("fullname", user.getFullname());
            json.put("birthday", user.getBirthday());
            json.put("avatar", user.getAvatar());
            json.put("company", user.getCompany());
            json.put("address", user.getAddress());
            json.put("employee", user.getEmployee());
            json.put("score", user.getScore());
            json.put("grade", user.getGrade());
            json.put("utype", user.getUtype());
            json.put("ctype", user.getCtype());
            json.put("source", user.getSource());
            json.put("lastLoginIp", user.getLastLoginIp());
            json.put("lastLoginTime", user.getLastLoginTime());
            result.add(json);
        }
        long total = bizUserMapper.countByExample(example);
        JSONObject json = new JSONObject();
        json.put("total", total);
        json.put("list", result);
        return new ResultBean(json);
    }

    @Override
    public ResultBean testRedis() throws Exception {
        Long count = redisCache.incr("111");
        if (count <= 1) {
            redisCache.set("111", redisCache.get("111"), 20);
        }
        if (count > 5) {
            System.out.println(count);
        }
        return new ResultBean(count);
    }


}
