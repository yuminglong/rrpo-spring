package com.jiebao.platfrom.wx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.domain.QueryRequest;
import com.jiebao.platfrom.common.utils.IDCard;
import com.jiebao.platfrom.system.dao.DeptMapper;
import com.jiebao.platfrom.system.domain.Dept;
import com.jiebao.platfrom.system.service.DeptService;
import com.jiebao.platfrom.wx.domain.People;
import com.jiebao.platfrom.wx.dao.PeopleMapper;
import com.jiebao.platfrom.wx.service.IPeopleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author qta
 * @since 2020-10-16
 */
@Service
public class PeopleServiceImpl extends ServiceImpl<PeopleMapper, People> implements IPeopleService {

    @Autowired
    DeptMapper deptMapper;
    @Autowired
    DeptService deptService;
    private static int lock = 1;//默认上锁

    @Override
    public JiebaoResponse listPage(QueryRequest queryRequest, String DeptId, Integer rank) {
        JiebaoResponse jiebaoResponse = new JiebaoResponse();
        QueryWrapper<People> queryWrapper = new QueryWrapper<>();
        Dept dept = null;
        if (DeptId == null) {
            dept = deptService.getDept();
            DeptId = dept.getDeptId();
        } else {
            dept = deptService.getById(DeptId);
        }
        rank = dept.getRank();
        String column = "";
        if (rank == 1)
            column = "shi";
        if (rank == 2)
            column = "qu_xian";
        if (rank == 3)
            column = "xiang";
        queryWrapper.eq(column, DeptId);
        queryWrapper.orderByDesc("age");
        Page<People> page = new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize());
        return jiebaoResponse.data(this.baseMapper.listPage(page, queryWrapper)).message("查询成功");
    }

    @Override
    public JiebaoResponse saveOrUpdateChile(People people) {
        JiebaoResponse jiebaoResponse = new JiebaoResponse();
        if (isNotLock())
            return jiebaoResponse.failMessage("时间锁定不可更改");
        if (people.getHlId() == null) {  //执行 身份证判断年龄
            try {
                Map<String, Object> map = IDCard.identityCard18(people.getIdCard());
                people.setAge((Integer) map.get("age"));
                people.setSex((String) map.get("sex"));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        jiebaoResponse = super.saveOrUpdate(people) ? jiebaoResponse.okMessage("操作成功").data(people) : jiebaoResponse.failMessage("操作失败").data(people);
        return jiebaoResponse;
    }



    @Override
    public JiebaoResponse lock(Integer status) {
        lock = status;
        return new JiebaoResponse().okMessage("操作成功");
    }

    @Override
    public boolean removeByIds(Collection<? extends Serializable> idList) {
        JiebaoResponse jiebaoResponse = new JiebaoResponse();
        if (isNotLock())
            return false;
        return super.removeByIds(idList);
    }

    @Override
    public JiebaoResponse checkLock() {
        JiebaoResponse jiebaoResponse = new JiebaoResponse();
        return isNotLock() ? jiebaoResponse.okMessage("状态锁定") : jiebaoResponse.failMessage("锁定解除");
    }

    private boolean isNotLock() { //判断当前是否上锁
        return lock == 1 ? true : false;
    }


}
