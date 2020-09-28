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
import com.jiebao.platfrom.wx.domain.UserI;
import com.jiebao.platfrom.wx.dao.UserIMapper;
import com.jiebao.platfrom.wx.service.IQunService;
import com.jiebao.platfrom.wx.service.IUserIService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
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
public class UserIServiceImpl extends ServiceImpl<UserIMapper, UserI> implements IUserIService {
    @Autowired
    IQunService qunService;

    @Override
    public boolean saveOrUpdate(UserI entity) {
        boolean a = false;//记录此次为修改还是添加
        if (entity.getWxUserId() == null) {
            entity.setDate(new Date());
            a = true;
        }
        boolean b = super.saveOrUpdate(entity);
        return b;
    }

    @Override
    public JiebaoResponse deleteS(String[] wxUserIdS, String qunId) {
        JiebaoResponse jiebaoResponse = new JiebaoResponse();
        boolean b = removeByIds(Arrays.asList(wxUserIdS));
        jiebaoResponse = b ? jiebaoResponse.okMessage("删除成功") : jiebaoResponse.failMessage("删除失败");
        return jiebaoResponse;
    }

    @Override
    public JiebaoResponse list(QueryRequest queryRequest, String name, String wxQunId) {
        QueryWrapper<UserI> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("qun_id", wxQunId);
        if (name != null) {
            queryWrapper.like("name", name);
        }
        Page<UserI> page = new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize());
        return new JiebaoResponse().data(page(page, queryWrapper));
    }
}
