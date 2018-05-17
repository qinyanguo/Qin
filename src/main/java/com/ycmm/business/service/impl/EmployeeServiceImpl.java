package com.ycmm.business.service.impl;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ycmm.base.exceptions.base.BadRequestException;
import com.ycmm.base.exceptions.base.ErrorMsgException;
import com.ycmm.base.system.UserLoginCheck;

import com.ycmm.business.mapper.BaseBizEmployeeMapper;
import com.ycmm.business.service.EmployeeService;
import com.ycmm.model.BaseBizEmployee;
import com.ycmm.model.BaseBizEmployeeExample;
import net.sf.json.JSONObject;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import com.ycmm.base.bean.BizParamBean;
import com.ycmm.base.bean.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    BaseBizEmployeeMapper baseBizEmployeeMapper;

    @Override
    public ResultBean create(BizParamBean bizParamBean) throws Exception {
        BaseBizEmployee employee = JSON.parseObject(bizParamBean.getBiz_param().toString(), BaseBizEmployee.class);
        employee.setStatus(1);
        BaseBizEmployeeExample example = new BaseBizEmployeeExample();
        BaseBizEmployeeExample.Criteria criteria = example.createCriteria();
        criteria.andNoEqualTo(employee.getNo()).andStatusEqualTo(1);
        List<BaseBizEmployee> list = baseBizEmployeeMapper.selectByExample(example);
        if (!list.isEmpty()) {
            throw new ErrorMsgException("工号重复！");
        }
        // 初始密码 123456 d93a5def7511da3d0f2d171d9c344e91
        employee.setPassword("d93a5def7511da3d0f2d171d9c344e91");
        if("".equals(employee.getLeavedate())){
			employee.setLeavedate(null);
		}
		if("".equals(employee.getEntrydate())){
        	employee.setEntrydate(null);
		}
        baseBizEmployeeMapper.insertSelective(employee);
        return new ResultBean();
    }

	@UserLoginCheck
	@Override
	public ResultBean queryEmployeeList(BizParamBean bizParamBean) throws Exception {
		int id = bizParamBean.getBiz_param().optInt("id", -1);
		String name = bizParamBean.getBiz_param().optString("name");
		String mobile = bizParamBean.getBiz_param().optString("mobile");
		String no = bizParamBean.getBiz_param().optString("no");
		Integer orgId = bizParamBean.getBiz_param().optInt("orgId");
		Integer leave = bizParamBean.getBiz_param().optInt("leave", -1);
		BaseBizEmployeeExample example = new BaseBizEmployeeExample();
		BaseBizEmployeeExample.Criteria criteria = example.createCriteria();
		if (id != -1) {
			criteria.andIdEqualTo(id);
		}
		if (StringUtils.isNotEmpty(name)) {
			criteria.andNameLike("%" + name + "%");
		}
		if (StringUtils.isNotEmpty(no)) {
			criteria.andNoEqualTo(no);
		}
		if (StringUtils.isNotEmpty(mobile)) {
			criteria.andMobileLike(mobile + "%");
		}
		if (leave != null && leave.intValue() >= 0) {
			criteria.andLeaveEqualTo(leave);
		}
		List<BaseBizEmployee> list = baseBizEmployeeMapper.selectByExample(example);
		 long total = baseBizEmployeeMapper.countByExample(example);
		JSONObject json = new JSONObject();
		json.put("total", total);
		json.put("list", list);
		return new ResultBean(json);
	}

    @Override
    public List<BaseBizEmployee> queryEmployeeList(BaseBizEmployee baseBizEmployee) throws Exception {
        BaseBizEmployeeExample example = new BaseBizEmployeeExample();
        BaseBizEmployeeExample.Criteria criteria = example.createCriteria();
        if (baseBizEmployee.getId() != null) {
            criteria.andIdEqualTo(baseBizEmployee.getId());
        }
        List<BaseBizEmployee> list = baseBizEmployeeMapper.selectByExample(example);
        return list;
    }

    @Override
    public List<Map<String, Object>> queryEmployeeExcel(BaseBizEmployee baseBizEmployee) throws Exception {
        try {
            BaseBizEmployeeExample example = new BaseBizEmployeeExample();
            BaseBizEmployeeExample.Criteria criteria = example.createCriteria();
            if (baseBizEmployee.getId() != null) {
                criteria.andIdEqualTo(baseBizEmployee.getId());
            }
            List<BaseBizEmployee> employees = baseBizEmployeeMapper.selectByExample(example);
            List<Map<String, Object>> list = new ArrayList<>();
            for (int i = 0; i < employees.size(); i++) {
                BaseBizEmployee bizEmployee =  employees.get(i);
                String string = JSON.toJSONString(bizEmployee, SerializerFeature.WriteDateUseDateFormat);
                Map<String, Object> map = JSON.parseObject(string, Map.class);
                list.add(map);
            }
            return list;
        }catch (Exception e) {
            e.printStackTrace();
        }

       return null;
    }


    @UserLoginCheck
	@Override
	public ResultBean queryEmployeeInfo(BizParamBean bizParamBean) throws Exception {
		int id = bizParamBean.getBiz_param().optInt("id", -1);
		if (id == -1) {
			throw new BadRequestException();
		}
		return new ResultBean(queryEmployeeInfo(id));
	}

	@Override
	public BaseBizEmployee queryEmployeeInfo(Integer employeeId) throws Exception {
		if (employeeId == null || employeeId <= 0) {
			return null;
		}
		BaseBizEmployee baseBizEmployee = baseBizEmployeeMapper.selectByPrimaryKey(employeeId);
		return baseBizEmployee;
	}


	@Override
	public ResultBean queryByName(BizParamBean bizParamBean) throws Exception {
		String name = bizParamBean.getBiz_param().optString("name", "");
		BaseBizEmployeeExample example = new BaseBizEmployeeExample();
		example.setOrderByClause("ctime DESC");
		BaseBizEmployeeExample.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotEmpty(name)) {
			criteria.andNameLike("%" + name + "%");
		}
		List<BaseBizEmployee> baseBizEmployee = baseBizEmployeeMapper.selectByExample(example);
		return new ResultBean(baseBizEmployee);
	}

	@Override
	public ResultBean updateEmployeeInfo(BizParamBean bizParamBean) throws Exception {
        BaseBizEmployee employee = JSON.parseObject(bizParamBean.getBiz_param().toString(), BaseBizEmployee.class);
		updateEmployee(employee);
		return new ResultBean("更新成功！");
	}

	private void updateEmployee(BaseBizEmployee employee) throws Exception {
		Integer id = employee.getId();
		if (id == null) {
			throw new BadRequestException();
		}
		// employee.setId(null);
		employee.setName(null);
		System.err.println(JSONObject.fromObject(employee));
		if (baseBizEmployeeMapper.updateByPrimaryKeySelective(employee) < 0) {
			throw new ErrorMsgException("更新客户详情失败！");
		}
	}

	@Override
	public ResultBean updatePassword(BizParamBean bizParamBean) throws Exception {
		JSONObject biz_param = bizParamBean.getBiz_param();
		int id = biz_param.optInt("id", -1);
		String oldPwd = biz_param.optString("oldPwd").trim();
		String newPwd = biz_param.optString("newPwd").trim();
		if (id == -1) {
			throw new BadRequestException();
		}
		if (StringUtils.isEmpty(oldPwd)) {
			throw new ErrorMsgException("请输入原密码！");
		}
		if (StringUtils.isEmpty(newPwd)) {
			throw new ErrorMsgException("请输入新密码！");
		}
		if (newPwd.length() < 6 && newPwd.length() > 12) {
			throw new ErrorMsgException("请输入6-12位的数字或字母");
		}
		BaseBizEmployee oldEmployee = queryEmployeeInfo(id);
		if (oldEmployee == null) {
			throw new ErrorMsgException("没有查询到此业务员！");
		}
		String shaHex = DigestUtils.shaHex(oldPwd);
		if (!DigestUtils.md5Hex(shaHex).equals(oldEmployee.getPassword())) {
			throw new ErrorMsgException("原密码密码不正确！");
		}
		shaHex = DigestUtils.shaHex(newPwd);
		oldEmployee.setPassword(DigestUtils.md5Hex(shaHex));
		updateEmployee(oldEmployee);

		return new ResultBean("修改密码成功！");
	}

    @Override
    public int update(BaseBizEmployee employee) throws Exception {
        return 0;
    }
}