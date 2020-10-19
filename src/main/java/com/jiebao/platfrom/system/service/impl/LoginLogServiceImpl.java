package com.jiebao.platfrom.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.domain.QueryRequest;
import com.jiebao.platfrom.common.utils.AddressUtil;
import com.jiebao.platfrom.common.utils.HttpContextUtil;
import com.jiebao.platfrom.common.utils.IPUtil;
import com.jiebao.platfrom.system.dao.LoginLogMapper;
import com.jiebao.platfrom.system.domain.*;
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
    public JiebaoResponse lists(String deptParentId, Integer year, Integer month) {
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
    public JiebaoResponse listUsers(QueryRequest queryRequest, String deptId, String startDate, String endDate) {
        JiebaoResponse jiebaoResponse = new JiebaoResponse();
        QueryWrapper<LoginLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dept_id", deptId);
        queryWrapper.orderByDesc("login_time");
        if (startDate != null)
            queryWrapper.ge("login_time", startDate);
        if (endDate != null)
            queryWrapper.le("login_time", endDate);
        Page<LoginLog> objectPage = new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize());
        return jiebaoResponse.data(this.baseMapper.loginCountUser(objectPage, queryWrapper)).okMessage("查询成功");
    }

    @Override
    public JiebaoResponse selectWeekCount(Integer year, Integer month) {
        JiebaoResponse jiebaoResponse = new JiebaoResponse();
        return jiebaoResponse.data(data(year, month)).okMessage("查询成功");
    }

    private List<Week> data(Integer year, Integer month) {
        Calendar calendar = Calendar.getInstance();//日历
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        int dayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);//月 天数
        int day = 1;
        calendar.set(Calendar.DAY_OF_MONTH, day);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK); //
        //一周的开始 1----7  西方周日 到周六   国内是  2-1 周一到周日
        List<Week> list = new ArrayList<>();
        Week week1 = new Week();
        week1.setStartDate(year + "-" + month + "-" + day);
        if (dayOfWeek == 1) {
            day += 6;
        } else {
            day += (8 - dayOfWeek);
        }
        week1.setEndDate(year + "-" + month + "-" + day++);
        list.add(week1);
        int yuDay = dayOfMonth - day + 1;//剩下的天数
        int weeks = yuDay / 7;//剩下的整数周
        int yu = yuDay % 7; //余数
        for (int j = 0; j < weeks; j++) {
            Week week = new Week();
            week.setStartDate(year + "-" + month + "-" + day);
            day += 6;
            week.setEndDate(year + "-" + month + "-" + day++);
            list.add(week);
        }
        if (yu != 0) {
            Week week = new Week();
            week.setStartDate(year + "-" + month + "-" + day);
            day += yu - 1;
            week.setEndDate(year + "-" + month + "-" + day++);
            list.add(week);
        }
        return list;
    }

    @Override
    public JiebaoResponse weekList(Integer year, Integer month, String deptId) {
        JiebaoResponse jiebaoResponse = new JiebaoResponse();
        List<Week> data = data(year, month);
        List<LogCount> list = new ArrayList<>();
        for (Week week : data   //不同周的具体时间
        ) {
            LogCount logCount = new LogCount(week.getStartDate() + " 至 " + week.getEndDate(), this.baseMapper.loginCountWeek(week.getStartDate(), week.getEndDate(), deptId));
            list.add(logCount);
        }
        return jiebaoResponse.okMessage("查询完毕").data(list);
    }


}
