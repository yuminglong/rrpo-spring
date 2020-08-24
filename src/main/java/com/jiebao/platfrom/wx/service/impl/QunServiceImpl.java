package com.jiebao.platfrom.wx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiebao.platfrom.common.authentication.JWTUtil;
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
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.stereotype.Service;

import java.util.Date;

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
    public boolean saveOrUpdate(Qun entity) {
        if (entity.getWxId() == null) {
            String username = JWTUtil.getUsername(SecurityUtils.getSubject().getPrincipal().toString());
            Dept dept = deptService.getById(userMapper.getDeptID(username));  //当前登陆人的部门
            entity.setDate(new Date());
            entity.setCjDeptId(dept.getDeptId());
            entity.setShDeptId(dept.getParentId());
        }
        return super.saveOrUpdate(entity);
    }

    @Override
    public JiebaoResponse pageList(QueryRequest queryRequest, String name, String userName) {
        String username = JWTUtil.getUsername(SecurityUtils.getSubject().getPrincipal().toString());
        Dept dept = deptService.getById(userMapper.getDeptID(username));  //当前登陆人的部门
        QueryWrapper<Qun> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("cj_dept_id", dept.getDeptId());
        queryWrapper.or();
        queryWrapper.eq("sh_dept_id", dept.getDeptId());
        if (name != null) {
            queryWrapper.like("wx_name", name);
        }
        if (userName != null) {
            queryWrapper.like("wx_user_name", userName);
        }
        queryWrapper.orderByDesc("date");
        Page<Qun> page = new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize());
        return new JiebaoResponse().data(page(page, queryWrapper)).okMessage("查询成功");
    }

    @Override
    public JiebaoResponse updateStatus(String qunId) {
        JiebaoResponse jiebaoResponse = new JiebaoResponse();
        Qun qun = getById(qunId);
        qun.setStatus(0);
        qun.setShDeptId(qun.getCjDeptId());
        jiebaoResponse = updateById(qun) ? jiebaoResponse.okMessage("操作成功") : jiebaoResponse.failMessage("操作失败");
        return jiebaoResponse;
    }
}
