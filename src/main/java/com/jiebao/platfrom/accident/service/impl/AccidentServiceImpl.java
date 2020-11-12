package com.jiebao.platfrom.accident.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiebao.platfrom.accident.dao.DeptSMapper;
import com.jiebao.platfrom.accident.daomain.ANumber;
import com.jiebao.platfrom.accident.daomain.Accident;
import com.jiebao.platfrom.accident.dao.AccidentMapper;
import com.jiebao.platfrom.accident.daomain.compareTable;
import com.jiebao.platfrom.accident.service.IAccidentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.domain.QueryRequest;
import com.jiebao.platfrom.system.domain.Dept;
import com.jiebao.platfrom.system.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
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
    public JiebaoResponse list(QueryRequest queryRequest, String policeId, String cityLevelId, String quDeptId, String startDate, String endDate) {
        QueryWrapper<Accident> queryWrapper = new QueryWrapper<>();
        if (quDeptId != null) {
            queryWrapper.eq("city_qx_id", quDeptId);
        } else if (cityLevelId != null) {
            queryWrapper.eq("city_cs_id", cityLevelId);
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
        queryWrapper.orderByDesc("date");
        Page<Accident> page = new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize());
        return new JiebaoResponse().data(this.baseMapper.ListPage(page, queryWrapper)).message("查询成功");
    }

    @Override
    public JiebaoResponse map(String policeId, String cityLevelId, String startDate, String endDate, String quDeptId) {  //视图接口
        QueryWrapper<Accident> queryWrapper = new QueryWrapper<>();
        JiebaoResponse jiebaoResponse = new JiebaoResponse();
        if (quDeptId != null) {
            queryWrapper.eq("city_qx_id", quDeptId);
        }
        if (cityLevelId != null) {
            queryWrapper.eq("city_cs_id", cityLevelId);
        } else if (policeId != null) {
            queryWrapper.in("city_cs_id", deptSMapper.selectDeptIds(policeId));
        }
        if (startDate != null) {
            queryWrapper.ge("date", startDate);    //不能小于此时间
        }
        if (endDate != null) {
            queryWrapper.le("date", endDate);//不能大于此时间
        }
        String[] stringDict = new String[]{"nature", "instation_section", "road", "age", "closed"};  //存储字典类
        for (String column : stringDict
        ) {
            QueryWrapper<Accident> clone = queryWrapper.clone();
            clone.groupBy(column);
            jiebaoResponse.put(column, this.baseMapper.listDict(clone, column));
        }
        String[] stringCg = new String[]{"jzd", "distance", "identity", "conditions"};  //常规
        for (String column : stringCg
        ) {
            QueryWrapper<Accident> clone = queryWrapper.clone();
            clone.groupBy(column);
            jiebaoResponse.put(column, this.baseMapper.listCg(clone, column));
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

    @Override
    public String func(String nature, String instationSection, String road, String age, String closed, String jzd, String distance, String identity, String conditions) {
        String sql = "";
        if (nature != null && !nature.equals("A"))
            return "B";
        else {
            if (nature != null && instationSection != null && nature.equals("A") && instationSection.equals("A"))
                return "B";
            else {
                if (road == null || age == null || closed == null ||
                        jzd == null || distance == null || identity == null ||
                        conditions == null)
                    return "";
                sql = "select dnfxxs from zd_dnfxxs where 1=1";
                sql += " and instr(zd3,'" + road + "')>0 ";
                sql += " and instr(zd4,'" + age + "')>0 ";
                sql += " and instr(zd5,'" + closed + "')>0 ";
                sql += " and instr(zd6,'" + jzd + "')>0 ";
                sql += " and instr(zd7,'" + distance + "')>0 ";
                sql += " and instr(zd8,'" + identity + "')>0 ";
                sql += " and instr(zd9,'" + conditions + "')>0 ";
                sql += " limit 1";
            }
        }
        return this.baseMapper.getXs(sql);
    }

    @Override
    public List<compareTable> compareTable(Integer startYear, Integer startMonth, Integer endYear, Integer endMonth) {
        endMonth+=1;
        List<compareTable> compareTables = this.baseMapper.shiTable(startYear + "-" + startMonth, endYear + "-" + endMonth);
        compareTables.addAll(this.baseMapper.gzTable(startYear + "-" + startMonth, endYear + "-" + endMonth));
        String startDate = startYear + "-" + startMonth;
        String endDate = endYear + "-" + endMonth;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
        try {
            Date parse = simpleDateFormat.parse(startDate);//本期开始
            Date parse1 = simpleDateFormat.parse(endDate);//本期结束
            long l = parse1.getTime() - parse.getTime();
            System.out.println(simpleDateFormat.format(parse));
            System.out.println(simpleDateFormat.format(parse1));
            System.out.println(l);
            Date parse2=new Date(parse.getTime()-l); //上期  开始
            Date parse3=new Date(parse1.getTime()-l);//上期  结束
            System.out.println(simpleDateFormat.format(parse2));
            System.out.println(simpleDateFormat.format(parse3));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return compareTables;
    }
}
