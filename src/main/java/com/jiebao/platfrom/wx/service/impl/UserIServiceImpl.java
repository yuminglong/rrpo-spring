package com.jiebao.platfrom.wx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.domain.QueryRequest;
import com.jiebao.platfrom.system.service.DeptService;
import com.jiebao.platfrom.wx.domain.Qun;
import com.jiebao.platfrom.wx.domain.UserI;
import com.jiebao.platfrom.wx.dao.UserIMapper;
import com.jiebao.platfrom.wx.service.IQunService;
import com.jiebao.platfrom.wx.service.IUserIService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
    @Autowired
    DeptService deptService;

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
    public JiebaoResponse list(QueryRequest queryRequest, String deptId, String name, String wxQunId) {
        QueryWrapper<UserI> queryWrapper = new QueryWrapper<>();
        if (wxQunId != null) {
            queryWrapper.eq("qun_id", wxQunId);
        }
        if (name != null) {
            queryWrapper.like("name", name);
        }
        if (deptId != null) {
            List<String> prentIdS = new ArrayList<>();
            List<String> deptIds = new ArrayList<>(); //存储id
            prentIdS.add(deptId);
            deptIds.add(deptId);
            deptService.getAllIds(prentIdS, deptIds);
            QueryWrapper<Qun> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.in("cj_dept_id", deptIds);
            List<String> list = this.baseMapper.listWxId(queryWrapper1);//所有的群的id
            queryWrapper.in("qun_id", list);
        }
        Page<UserI> page = new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize());
        return new JiebaoResponse().data(page(page, queryWrapper));
    }
}
