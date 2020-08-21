package com.jiebao.platfrom.check.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiebao.platfrom.check.domain.Num;
import com.jiebao.platfrom.check.dao.NumMapper;
import com.jiebao.platfrom.check.service.INumService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiebao.platfrom.common.authentication.JWTUtil;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.domain.QueryRequest;
import com.jiebao.platfrom.system.dao.UserMapper;
import com.jiebao.platfrom.system.domain.Dept;
import com.jiebao.platfrom.system.domain.User;
import com.jiebao.platfrom.system.service.DeptService;
import com.jiebao.platfrom.system.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author qta
 * @since 2020-07-28
 */
@Service
public class NumServiceImpl extends ServiceImpl<NumMapper, Num> implements INumService {
    @Autowired
    UserService userService;
    @Autowired
    UserMapper userMapper;
    @Autowired
    DeptService deptService;

    @Override
    public JiebaoResponse pageList(QueryRequest queryRequest, String deptId, String yearId) {
        String username = JWTUtil.getUsername((String) SecurityUtils.getSubject().getPrincipal());  //当前登陆人名字
        Dept dept = deptService.getById(userMapper.getDeptID(username));  //当前登陆人的部门
//        if (!dept.getParentId().equals("-1")) {  //当前登陆人非最高级
//            deptId = dept.getDeptId();
//        }
        QueryWrapper<Num> queryWrapper = new QueryWrapper<>();
        if (deptId != null) {
            queryWrapper.eq("dept_id", deptId);
        }
        if (yearId != null) {
            queryWrapper.eq("year_id", yearId);
        }
        queryWrapper.orderByDesc("year_date");
        Page<Num> page = new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize());
        return new JiebaoResponse().data(this.baseMapper.pageList(page, queryWrapper)).message("查询成功");
    }
}
