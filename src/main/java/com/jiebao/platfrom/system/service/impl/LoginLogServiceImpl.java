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
import java.util.Calendar;
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
        Integer place = null;  //名次
        if (deptParentId.equals("0")) {
            place = 1;  //省级
        } else if (deptService.getById(deptParentId).getParentId().equals("0")) {
            //二级
            place = 2;  //市级
        } else if ((deptService.getById(deptService.getById(deptParentId).getParentId()).getParentId()).equals("0")) {
            place = 3;//县级    //查询时  须精确到人-
        } else {
            return jiebaoResponse.failMessage("查询组织溢出");
        }
        List<Dept> childrenList = deptService.getChildrenList(deptParentId);//得到下面的子集
        List<LoginCount> listLoginCount = new ArrayList<>();
        listLoginCount.add(this.baseMapper.loginCountPrent(deptParentId));//把本级显示出来
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
            listLoginCount.add(this.baseMapper.loginCount(queryWrapper, dept.getDeptName(), deptId));
        }
        String prentId = null;
        if (deptParentId.equals("0"))
            prentId = "0";
        else
            prentId = deptService.getById(deptParentId).getParentId();
        return jiebaoResponse.data(listLoginCount).put("place", place).put("prentId", prentId).okMessage("查询成功");
    }

    @Override
    public JiebaoResponse listUsers(String deptId, Date startDate, Date endDate) {
        JiebaoResponse jiebaoResponse = new JiebaoResponse();
        QueryWrapper<LoginLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dept_id", deptId);
        queryWrapper.orderByDesc("login_time");
        return jiebaoResponse.data(this.baseMapper.loginCountUser(queryWrapper)).okMessage("查询成功");
    }

    @Override
    public JiebaoResponse selectWeekCount(String year) {
        return null;
    }

    public static void main(String[] args) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2020);
        calendar.set(Calendar.MONTH, 8);
        calendar.set(Calendar.DAY_OF_MONTH, 3);
        System.out.println(calendar.get(Calendar.DAY_OF_YEAR));
        System.out.println(calendar.get(Calendar.WEEK_OF_MONTH));
        System.out.println(calendar.get(Calendar.DAY_OF_WEEK));
    }

}
