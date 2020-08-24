package com.jiebao.platfrom.wx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiebao.platfrom.common.authentication.JWTUtil;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.domain.QueryRequest;
import com.jiebao.platfrom.railway.domain.Address;
import com.jiebao.platfrom.system.dao.UserMapper;
import com.jiebao.platfrom.system.domain.Dept;
import com.jiebao.platfrom.system.service.DeptService;
import com.jiebao.platfrom.system.service.UserService;
import com.jiebao.platfrom.wx.domain.Month;
import com.jiebao.platfrom.wx.dao.MonthMapper;
import com.jiebao.platfrom.wx.service.IMonthService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author qta
 * @since 2020-08-22
 */
@Service
public class MonthServiceImpl extends ServiceImpl<MonthMapper, Month> implements IMonthService {
    @Autowired
    DeptService deptService;
    @Autowired
    UserMapper userMapper;
    @Autowired
    MonthMapper monthMapper;

    @Override
    public boolean saveOrUpdate(Month entity) {
        if (entity.getWxMonthId() == null) {
            String username = JWTUtil.getUsername(SecurityUtils.getSubject().getPrincipal().toString());
            Dept dept = deptService.getById(userMapper.getDeptID(username));  //当前登陆人的部门
            entity.setDept(dept.getDeptId());
            entity.setShDept(dept.getParentId());
            entity.setDate(new Date());
        }
        return super.saveOrUpdate(entity);
    }

    @Override
    public JiebaoResponse pageList(QueryRequest queryRequest, String month) {
        QueryWrapper<Month> queryWrapper = new QueryWrapper<>();
        String username = JWTUtil.getUsername(SecurityUtils.getSubject().getPrincipal().toString());
        Dept dept = deptService.getById(userMapper.getDeptID(username));  //当前登陆人的部门
        queryWrapper.eq("dept", dept.getDeptId());
        queryWrapper.or();
        queryWrapper.eq("sh_dept", dept.getDeptId());
        if (month != null) {
            queryWrapper.eq("month", month);
        }
        queryWrapper.orderByDesc("date");
        Page<Month> page = new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize());
        return new JiebaoResponse().data(page(page, queryWrapper)).message("查询成功");
    }

    @Override
    public JiebaoResponse appear(String monthId) {
        JiebaoResponse jiebaoResponse = new JiebaoResponse();
        Month month = getById(monthId);
        String username = JWTUtil.getUsername(SecurityUtils.getSubject().getPrincipal().toString());
        Dept dept = deptService.getById(userMapper.getDeptID(username));  //当前登陆人的部门
        if (!month.getShDept().equals(dept.getDeptId())) {
            return jiebaoResponse.failMessage("无权上报");
        }
        if (dept.getParentId().equals("0")) {  //此为 市级
            if (monthMapper.count(month.getMonth(), dept.getDeptId()) >= 3) {
                return jiebaoResponse.failMessage("超过上报三条记录上线");
            }
            month.setLastDept(dept.getDeptId());
        }
        month.setShDept(dept.getParentId());
        return jiebaoResponse.okMessage("上报成功");
    }

    @Override
    public JiebaoResponse tgList(QueryRequest queryRequest) {
        String username = JWTUtil.getUsername(SecurityUtils.getSubject().getPrincipal().toString());
        Dept dept = deptService.getById(userMapper.getDeptID(username));  //当前登陆人的部门
        List<Address> address = deptService.getAddress(dept.getDeptId()); //附属id
        QueryWrapper<Month> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("dept", address);
        Page<Month> page = new Page<>();
        return new JiebaoResponse().message("查询成功").data(page(page, queryWrapper));
    }
}
