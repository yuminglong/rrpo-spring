package com.jiebao.platfrom.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.utils.AddressUtil;
import com.jiebao.platfrom.common.utils.HttpContextUtil;
import com.jiebao.platfrom.common.utils.IPUtil;
import com.jiebao.platfrom.system.dao.LoginLogMapper;
import com.jiebao.platfrom.system.domain.Dept;
import com.jiebao.platfrom.system.domain.LoginCount;
import com.jiebao.platfrom.system.domain.LoginLog;
import com.jiebao.platfrom.system.service.DeptService;
import com.jiebao.platfrom.system.service.LoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("loginLogService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class LoginLogServiceImpl extends ServiceImpl<LoginLogMapper, LoginLog> implements LoginLogService {
    @Autowired
    DeptService deptService;

    @Override
    @Transactional
    public void saveLoginLog(LoginLog loginLog) {
        loginLog.setLoginTime(new Date());
        HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
        String ip = IPUtil.getIpAddr(request);
        loginLog.setIp(ip);
        loginLog.setLocation(AddressUtil.getCityInfo(ip));
        this.save(loginLog);
    }

    @Override
    public JiebaoResponse lists(String deptParentId, Date startDate, Date endDate) {
        JiebaoResponse jiebaoResponse = new JiebaoResponse();
        List<Dept> childrenList = deptService.getChildrenList(deptParentId);//得到下面的子集
        List<LoginCount> listLoginCount = new ArrayList<>();
        for (Dept dept : childrenList
        ) {
            String deptId = dept.getDeptId();//部门id
            List<String> list = new ArrayList<>();
            List<String> deptS = new ArrayList<>();
            deptS.add(deptId);
            list.add(deptId);
            deptService.getAllIds(deptS, list);
            QueryWrapper<LoginLog> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("dept_id", list);
            listLoginCount.add(this.baseMapper.loginCount(queryWrapper).setName(dept.getDeptName()));
        }
        Integer place = null;  //名次
        if (deptParentId.equals("0"))  //省级第一级
            place = 1;  //省级
        if (deptService.getById(deptParentId).getParentId().equals("0")) //二级
            place = 2;  //市级
        if (deptService.getById(deptService.getById(deptParentId).getParentId()).getParentId().equals("0"))
            place = 3;//县级
        return jiebaoResponse.data(listLoginCount).put("place", place);
    }
}
