package com.jiebao.platfrom.wx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiebao.platfrom.common.authentication.JWTUtil;
import com.jiebao.platfrom.common.domain.JiebaoConstant;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.domain.QueryRequest;
import com.jiebao.platfrom.system.dao.UserMapper;
import com.jiebao.platfrom.system.domain.Dept;
import com.jiebao.platfrom.system.service.DeptService;
import com.jiebao.platfrom.system.service.UserService;
import com.jiebao.platfrom.wx.domain.Qun;
import com.jiebao.platfrom.wx.dao.QunMapper;
import com.jiebao.platfrom.wx.service.IQunService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author qta
 * @since 2020-08-20
 */
@Service
public class QunServiceImpl extends ServiceImpl<QunMapper, Qun> implements IQunService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    DeptService deptService;

    @Override
    public JiebaoResponse addOrUpdate(Qun entity) {
        JiebaoResponse jiebaoResponse = new JiebaoResponse();
        String username = JWTUtil.getUsername(SecurityUtils.getSubject().getPrincipal().toString());
        Dept dept = deptService.getById(userMapper.getDeptID(username));  //当前登陆人的部门
        if (entity.getWxId() == null) {
            if (!judge(dept.getDeptId())) {
                return jiebaoResponse.failMessage("此单位已建立群");
            }
            entity.setDate(new Date());
            entity.setCjDeptId(dept.getDeptId());
            entity.setShDeptId(dept.getDeptId());
            entity.setShStatus(0);
            entity.setNumber(0);
        } else {
            if (entity.getCjDeptId() != null && !entity.getCjDeptId().equals(dept.getDeptId())) {
                if (!judge(dept.getDeptId())) {
                    return jiebaoResponse.failMessage("此单位已建立群");
                }
            }
        }
        jiebaoResponse = super.saveOrUpdate(entity) ? jiebaoResponse.okMessage("操作成功") : jiebaoResponse.failMessage("操作失败");
        return jiebaoResponse;
    }

    private boolean judge(String deptId) {
        return this.baseMapper.judge(deptId) == null ? true : false;
    }

    @Override
    public JiebaoResponse pageList(QueryRequest queryRequest, String name, String userName, Integer Status) {
        String username = JWTUtil.getUsername(SecurityUtils.getSubject().getPrincipal().toString());
        Dept dept = deptService.getById(userMapper.getDeptID(username));  //当前登陆人的部门
        QueryWrapper<Qun> queryWrapper = new QueryWrapper<>();
        List<String> list = new ArrayList<>();
        List<String> ids = new ArrayList<>();
        ids.add(dept.getDeptId());
        deptService.getAllIds(ids, list);//当前部门的所有子集部门
        if (list.size() == 0) {
            queryWrapper.and(qunQueryWrapper -> qunQueryWrapper.eq("cj_dept_id", dept.getDeptId()).or().eq("sh_dept_id", dept.getDeptId()));
        } else {
            queryWrapper.and(qunQueryWrapper -> qunQueryWrapper.eq("cj_dept_id", dept.getDeptId()).or().eq("sh_dept_id", dept.getDeptId())
                    .or().in("cj_dept_id", list).eq("sh_status", 3));
        }

        if (name != null) {
            queryWrapper.like("wx_name", name);
        }
        if (userName != null) {
            queryWrapper.like("wx_user_name", userName);
        }
        if (Status != null) {
            queryWrapper.eq("sh_status", Status);
        }
        queryWrapper.orderByDesc("date");
        Page<Qun> page = new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize());
        return new JiebaoResponse().data(this.baseMapper.list(page, queryWrapper)).okMessage("查询成功");
    }


    @Override
    public JiebaoResponse updateStatus(String qunId) {
        JiebaoResponse jiebaoResponse = new JiebaoResponse();
        String username = JWTUtil.getUsername(SecurityUtils.getSubject().getPrincipal().toString());
        Dept dept = deptService.getById(userMapper.getDeptID(username));  //当前登陆人的部门
        if (dept.getDeptId().equals(getById(qunId).getCjDeptId())) {
            return jiebaoResponse.failMessage("无权发起重新提交");
        }
        Qun qun = getById(qunId);
        qun.setStatus(0);
        qun.setShDeptId(qun.getCjDeptId());
        jiebaoResponse = updateById(qun) ? jiebaoResponse.okMessage("操作成功") : jiebaoResponse.failMessage("操作失败");
        return jiebaoResponse;
    }

    @Override
    public JiebaoResponse up(String qunId) {
        JiebaoResponse jiebaoResponse = new JiebaoResponse();
        Qun qun = getById(qunId);
        String username = JWTUtil.getUsername(SecurityUtils.getSubject().getPrincipal().toString());
        Dept dept = deptService.getById(userMapper.getDeptID(username));  //当前登陆人的部门
        qun.setShDeptId(dept.getParentId());
        jiebaoResponse = updateById(qun) ? jiebaoResponse.okMessage("上报成功") : jiebaoResponse.failMessage("上报失败");
        return jiebaoResponse;
    }
}
