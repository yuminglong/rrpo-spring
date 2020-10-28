package com.jiebao.platfrom.accident.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiebao.platfrom.accident.dao.DeptSMapper;
import com.jiebao.platfrom.accident.daomain.ANumber;
import com.jiebao.platfrom.accident.daomain.Accident;
import com.jiebao.platfrom.accident.dao.AccidentMapper;
import com.jiebao.platfrom.accident.service.IAccidentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.domain.QueryRequest;
import com.jiebao.platfrom.system.domain.Dept;
import com.jiebao.platfrom.system.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author qta
 * @since 2020-08-04
 */
@Service
public class AccidentServiceImpl extends ServiceImpl<AccidentMapper, Accident> implements IAccidentService {
    @Autowired
    DeptService deptService;
    @Autowired
    DeptSMapper deptSMapper;

    @Override
    public JiebaoResponse list(QueryRequest queryRequest, String policeId, String cityLevelId, String startDate, String endDate) {
        QueryWrapper<Accident> queryWrapper = new QueryWrapper<>();
        if (cityLevelId != null) {
            queryWrapper.eq("city_cs_id", deptSMapper.selectDeptId(cityLevelId));
        } else if (policeId != null) {
            queryWrapper.in("city_cs_id", deptSMapper.selectDeptIds(policeId));
        } else {  //
            Dept dept = deptService.getDept();
            if (!dept.getDeptId().equals("0"))
                queryWrapper.eq("city_cs_id", dept.getDeptId());
        }
        if (startDate != null) {
            queryWrapper.ge("date", startDate);    //不能小于此时间
        }
        if (endDate != null) {
            queryWrapper.le("date", endDate);//不能大于此时间
        }
        Page<Accident> page = new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize());
        return new JiebaoResponse().data(this.baseMapper.ListPage(page, queryWrapper)).message("查询成功");
    }

    @Override
    public JiebaoResponse map(String policeId, String cityLevelId, String startDate, String endDate) {  //视图接口
        QueryWrapper<Accident> queryWrapper = new QueryWrapper<>();
        JiebaoResponse jiebaoResponse = new JiebaoResponse();
        if (cityLevelId != null) {
            queryWrapper.eq("city_cs_id", deptSMapper.selectDeptId(cityLevelId));
        } else {
            if (policeId != null) {
                queryWrapper.in("city_cs_id", deptSMapper.selectDeptIds(policeId));
            }
        }
        if (startDate != null) {
            queryWrapper.ge("date", startDate);    //不能小于此时间
        }
        if (endDate != null) {
            queryWrapper.le("date", endDate);//不能大于此时间
        }
        String[] strings = new String[]{"nature", "instation_section", "road", "age", "closed", "jzd", "distance", "identity", "conditions"};
        for (String column : strings
        ) {
            QueryWrapper<Accident> clone = queryWrapper.clone();
            clone.groupBy(column);
            jiebaoResponse.put(column, this.baseMapper.listAcc(clone, column));
        }
        return jiebaoResponse.okMessage("查询成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JiebaoResponse lock(String[] accidentId, String month, Integer status) {//上锁
        JiebaoResponse jiebaoResponse = new JiebaoResponse();
        UpdateWrapper<Accident> qw = new UpdateWrapper<>();
        qw.set("statu", status);
        if (accidentId != null && accidentId.length != 0) {
            qw.in("accident_id", Arrays.asList(accidentId));
        } else {
            if (month == null) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
                month = simpleDateFormat.format(new Date());
            }
            qw.likeRight("date", month);
        }
        jiebaoResponse = update(qw) ? jiebaoResponse.okMessage("操作成功") : jiebaoResponse.failMessage("操作失败");
        return jiebaoResponse;
    }

    public static void main(String[] args) {
        System.out.println(0.1 + 2);
    }


}
