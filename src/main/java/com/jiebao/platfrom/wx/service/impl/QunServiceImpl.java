package com.jiebao.platfrom.wx.service.impl;

import com.jiebao.platfrom.common.authentication.JWTUtil;
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
            entity.setShDateId(dept.getParentId());
        }
        return super.saveOrUpdate(entity);
    }
}
