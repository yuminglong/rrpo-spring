package com.jiebao.platfrom.wx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiebao.platfrom.common.authentication.JWTUtil;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.domain.QueryRequest;
import com.jiebao.platfrom.railway.domain.Address;
import com.jiebao.platfrom.system.dao.UserMapper;
import com.jiebao.platfrom.system.domain.Dept;
import com.jiebao.platfrom.system.domain.File;
import com.jiebao.platfrom.system.service.DeptService;
import com.jiebao.platfrom.system.service.FileService;
import com.jiebao.platfrom.system.service.UserService;
import com.jiebao.platfrom.wx.domain.Month;
import com.jiebao.platfrom.wx.dao.MonthMapper;
import com.jiebao.platfrom.wx.service.IMonthService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;

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
    FileService fileService;


    @Override
    public boolean saveOrUpdate(Month entity) {
        if (entity.getWxMonthId() == null) {
            String username = JWTUtil.getUsername(SecurityUtils.getSubject().getPrincipal().toString());
            Dept dept = deptService.getById(userMapper.getDeptID(username));  //当前登陆人的部门
            entity.setJcDeptId(dept.getDeptId());
            entity.setShDeptId(dept.getParentId());
            entity.setDate(new Date());
        }
        boolean b = super.saveOrUpdate(entity);
        if (b && entity.getFileIds() != null) {
            UpdateWrapper<File> updateWrapper = new UpdateWrapper<>();
            updateWrapper.in("file_id", Arrays.asList(entity.getFileIds()));
            updateWrapper.set("ref_id", entity.getWxMonthId());
            fileService.update(updateWrapper);
        }
        return b;
    }

    @Override
    public JiebaoResponse pageList(QueryRequest queryRequest, String month, Integer look, Integer status) {
        QueryWrapper<Month> queryWrapper = new QueryWrapper<>();
        String username = JWTUtil.getUsername(SecurityUtils.getSubject().getPrincipal().toString());
        Dept dept = deptService.getById(userMapper.getDeptID(username));  //当前登陆人的部门
        List<Dept> childrenList = deptService.getChildrenList(dept.getDeptId());//当前部门的所有子集部门
        List<String> resolver = resolver(childrenList);
        if (resolver.size() != 0) {
            queryWrapper.and(monthQueryWrapper -> monthQueryWrapper.eq("jc_dept_id", dept.getDeptId()).or().eq("sh_dept_id", dept.getDeptId())
                    .or().in("jc_dept_id", resolver).eq("status", 1));
        } else {
            queryWrapper.and(monthQueryWrapper -> monthQueryWrapper.eq("jc_dept_id", dept.getDeptId()).or().eq("sh_dept_id", dept.getDeptId())
            );
        }
        if (month != null) {
            queryWrapper.eq("month", month);
        }
        if (status != null) {
            queryWrapper.eq("status", status);
        }
        if (look != null) {
            queryWrapper.eq("look", look);
        }
        queryWrapper.orderByDesc("date");
        Page<Month> page = new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize());
        return new JiebaoResponse().data(this.baseMapper.list(page, queryWrapper)).message("查询成功");
    }

    private List<String> resolver(List<Dept> list) {
        List<String> listR = new ArrayList<>(); //储存数据
        for (Dept dept : list
        ) {
            listR.add(dept.getDeptId());
        }
        return listR;
    }

    @Override
    public JiebaoResponse appear(String monthId, Integer status) {
        String username = JWTUtil.getUsername(SecurityUtils.getSubject().getPrincipal().toString());
        Dept dept = deptService.getById(userMapper.getDeptID(username));  //当前登陆人的部门
        JiebaoResponse jiebaoResponse = new JiebaoResponse();
        Month month = getById(monthId);
        if (!month.getShDeptId().equals(dept.getDeptId())) {
            return jiebaoResponse.failMessage("无权上报");
        }
        if (dept.getParentId().equals("0")) {  //此为 市级
            if (this.baseMapper.count(month.getMonth(), dept.getDeptId()) >= 3) {
                return jiebaoResponse.failMessage("超过上报三条记录上限");
            }
        }
        if (status == 1) {  //赞成  //
            if (dept.getParentId().equals("-1")) {//省级
                month.setStatus(1);
            } else {  //非省级 继续上报
                month.setStatus(2);
                month.setShDeptId(dept.getParentId());
            }
        } else {
            month.setStatus(0);
        }
        updateById(month);
        return jiebaoResponse.okMessage("操作成功");
    }

    @Override
    public Month getById(Serializable id) {
        Month month = super.getById(id);
        month.setLook(1);
        updateById(month);
        month.setFileList(fileService.getAppendixList(month.getWxMonthId()));
        return month;
    }
}
