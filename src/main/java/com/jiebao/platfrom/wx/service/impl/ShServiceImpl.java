package com.jiebao.platfrom.wx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiebao.platfrom.common.authentication.JWTUtil;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.domain.QueryRequest;
import com.jiebao.platfrom.system.dao.UserMapper;
import com.jiebao.platfrom.system.domain.Dept;
import com.jiebao.platfrom.system.service.DeptService;
import com.jiebao.platfrom.wx.dao.QunJsMapper;
import com.jiebao.platfrom.wx.domain.Qun;
import com.jiebao.platfrom.wx.domain.QunJs;
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
    @Autowired
    QunJsMapper qunJsMapper;


    @Override
    public JiebaoResponse shWx(String qunId, Integer status, String massage) {
        JiebaoResponse jiebaoResponse = new JiebaoResponse();
        Dept dept = deptService.getDept();  //当前登陆人的部门
        Qun qun = qunService.getById(qunId);
        if (!dept.getDeptId().equals(qun.getShDeptId())) {
            return jiebaoResponse.message("无权限审核");
        }
        Sh sh = new Sh();//创建节点
        sh.setDeptId(dept.getDeptId());
        sh.setWxQunId(qunId);
        sh.setStatus(status);
        sh.setMassage(massage);
        sh.setShDate(new Date());
        save(sh);
        qun.setShDate(new Date());
        if (status == 0) {
            qun.setShDeptId(dept.getParentId());
            if (dept.getParentId().equals("-1")) {
                qun.setShStatus(3);
                QunJs qunJs = qunJsMapper.selectByWxId(qunId);
                qunJs.setSbyj(massage);
                qunService.updateById(qun);
                return jiebaoResponse.okMessage("审核全部完成");
            }
            qun.setShStatus(1);
            qunService.updateById(qun);
            return jiebaoResponse.okMessage("审核节点完成");
        } else {
            qun.setShStatus(2);
            qun.setShDeptId(qun.getCjDeptId());
            qunService.updateById(qun);
            return jiebaoResponse.okMessage("审核不通过 打回原籍");
        }
    }

    @Override
    public JiebaoResponse list(QueryRequest queryRequest, String qunId) {
        QueryWrapper<Sh> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("sh_date");
        queryWrapper.eq("wx_qun_id", qunId);
        Page<Sh> page = new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize());
        return new JiebaoResponse().data(this.baseMapper.list(page, queryWrapper)).message("查询成功");
    }
}
