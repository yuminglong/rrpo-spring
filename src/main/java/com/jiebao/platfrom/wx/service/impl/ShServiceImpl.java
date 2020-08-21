package com.jiebao.platfrom.wx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiebao.platfrom.common.authentication.JWTUtil;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.domain.QueryRequest;
import com.jiebao.platfrom.system.dao.UserMapper;
import com.jiebao.platfrom.system.domain.Dept;
import com.jiebao.platfrom.system.service.DeptService;
import com.jiebao.platfrom.wx.domain.Qun;
import com.jiebao.platfrom.wx.domain.Sh;
import com.jiebao.platfrom.wx.dao.ShMapper;
import com.jiebao.platfrom.wx.service.IQunService;
import com.jiebao.platfrom.wx.service.IShService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ShServiceImpl extends ServiceImpl<ShMapper, Sh> implements IShService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    DeptService deptService;
    @Autowired
    IQunService qunService;

    @Override
    public JiebaoResponse shWx(String qunId, Integer status, String massage) {
        JiebaoResponse jiebaoResponse = new JiebaoResponse();
        String username = JWTUtil.getUsername(SecurityUtils.getSubject().getPrincipal().toString());
        Dept dept = deptService.getById(userMapper.getDeptID(username));  //当前登陆人的部门
        Sh sh = new Sh();
        sh.setDeptId(dept.getDeptId());
        sh.setWxQunId(qunId);
        sh.setStatus(status);
        sh.setMassage(massage);
        sh.setShDate(new Date());
        Qun qun = qunService.getById(qunId);
        qun.setShDate(new Date());
        if (status == 0) {
            if (dept.getParentId().equals("0")) {
                qun.setShStatus(3);
                return jiebaoResponse.okMessage("审核全部完成");
            }
            qun.setShStatus(1);
            return jiebaoResponse.okMessage("审核节点完成");
        } else {
            qun.setShStatus(2);
            qun.setShDateId(qun.getCjDeptId());
            return jiebaoResponse.okMessage("审核不通过 打回原籍");
        }
    }

    @Override
    public JiebaoResponse list(QueryRequest queryRequest) {
        QueryWrapper<Sh> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("sh_date");
        Page<Sh> page = new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize());
        return new JiebaoResponse().data(page(page, queryWrapper));
    }
}
