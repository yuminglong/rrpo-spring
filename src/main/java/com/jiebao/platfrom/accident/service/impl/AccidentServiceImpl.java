package com.jiebao.platfrom.accident.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiebao.platfrom.accident.dao.AccidentMapper;
import com.jiebao.platfrom.accident.dao.DeptSMapper;
import com.jiebao.platfrom.accident.daomain.Accident;
import com.jiebao.platfrom.accident.daomain.compareTable;
import com.jiebao.platfrom.accident.service.IAccidentService;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.domain.QueryRequest;
import com.jiebao.platfrom.system.domain.Dept;
import com.jiebao.platfrom.system.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
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

    /**
     *  //返回  本期  上期表
     * @param startYear  开始年月
     * @param endYear  结束年月
     * @return
     */
    @Override
    public List<compareTable> compareTable(String startYear, String endYear) {
        List<compareTable> compareTables = this.baseMapper.shiTable(startYear, endYear);  //获得 市州 本期
        compareTables.addAll(this.baseMapper.gzTable(startYear, endYear));//公安处本期
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-00");
        Map<String, compareTable> map = new HashMap<>();
        for (compareTable compareTable : compareTables) {
            map.put(compareTable.getDeptName(), compareTable);
        }
        try {
            Date parse = simpleDateFormat.parse(startYear);//本期开始
            Date parse1 = simpleDateFormat.parse(endYear);//本期结束
            long l = parse1.getTime() - parse.getTime();
            Date parse2 = new Date(parse.getTime() - l); //上期  开始
            Date parse3 = new Date(parse1.getTime() - l);//上期  结束
            String upStartDate = simpleDateFormat.format(parse2);
            String upendDate = simpleDateFormat.format(parse3);
            List<compareTable> list = new ArrayList<>();
            list.addAll(this.baseMapper.shiTableUP(upStartDate, upendDate)); //市州上期
            list.addAll(this.baseMapper.gzTableUP(upStartDate, upendDate));//公安处上期
            compareTable compareTableCount = new compareTable("全省合计", 0,
                    0, 0.00,
                    0.00, 0.00,
                    0.00, 0.00,
                    0.00, 0.00,
                    0.00, 0.00,
                    0.00); //计算总的
            DecimalFormat decimalFormat = new DecimalFormat("0.0000");
            for (compareTable compareTable : list) {
                compareTable compareTable1 = map.get(compareTable.getDeptName());//已经保存的数值
                if (compareTable1 == null) {
                    continue;
                }
                compareTable1.setUpNumber(compareTable.getUpNumber() == null ? 0 : compareTable.getUpNumber());
                compareTable1.setUpDnxs(compareTable.getUpDnxs() == null ? 0 : compareTable.getUpDnxs());
                compareTable1.setUpDnTjxs(compareTable.getUpDnTjxs() == null ? 0 : compareTable.getUpDnTjxs());
                compareTable1.setUpDeathToll(compareTable.getUpDeathToll() == null ? 0 : compareTable.getUpDeathToll());
                double i = (double) (compareTable1.getUpNumber() - compareTable1.getNumber()) / compareTable1.getUpNumber();
                compareTable1.setBj1(Math.abs(i));
                compareTable1.setBj2(Double.parseDouble(decimalFormat.format(Math.abs((compareTable1.getUpDnxs() - compareTable1.getDnxs()) / compareTable1.getUpDnxs()))));
                compareTable1.setBj3(Double.parseDouble(decimalFormat.format(Math.abs((compareTable1.getUpDnTjxs() - compareTable1.getDntjxs()) / compareTable1.getUpDnTjxs()))));
                compareTable1.setBj4(Double.parseDouble(decimalFormat.format(Math.abs((compareTable1.getUpDeathToll() - compareTable1.getDeathToll()) / compareTable1.getUpDeathToll()))));
                if (compareTable1.getDeptName().contains("公安处")) {  //公安处级别
                    compareTableCount.setNumber(compareTableCount.getNumber() + compareTable1.getNumber());
                    compareTableCount.setUpNumber(compareTableCount.getUpNumber() + compareTable1.getUpNumber());
                    compareTableCount.setBj1(Double.parseDouble(decimalFormat.format(Math.abs((double) (compareTableCount.getUpNumber() - compareTableCount.getNumber()) / compareTableCount.getUpNumber()))));

                    compareTableCount.setDnxs(compareTableCount.getDnxs() + compareTable1.getDnxs());
                    compareTableCount.setUpDnxs(compareTableCount.getUpDnxs() + compareTable1.getUpDnxs());
                    compareTableCount.setBj2(Double.parseDouble(decimalFormat.format(Math.abs((compareTableCount.getUpDnxs() - compareTableCount.getDnxs()) / compareTableCount.getUpDnxs()))));

                    compareTableCount.setDntjxs(compareTableCount.getDntjxs() + compareTable1.getDntjxs());
                    compareTableCount.setUpDnTjxs(compareTableCount.getUpDnTjxs() + compareTable1.getDntjxs());
                    compareTableCount.setBj3(Double.parseDouble(decimalFormat.format(Math.abs((compareTableCount.getUpDnTjxs() - compareTableCount.getDntjxs()) / compareTableCount.getUpDnxs()))));

                    compareTableCount.setDeathToll(compareTableCount.getDeathToll() + compareTable1.getDeathToll());
                    compareTableCount.setUpDeathToll(compareTableCount.getUpDeathToll() + compareTable1.getUpDeathToll());
                    compareTableCount.setBj4(Double.parseDouble(decimalFormat.format(Math.abs(((compareTableCount.getUpDeathToll() + compareTable1.getDeathToll()) / compareTableCount.getUpDeathToll())))));
                }
            }
            compareTables.add(compareTableCount);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        for (compareTable compareTable : compareTables) {
            System.out.println(compareTable);
        }

        return compareTables;
    }


}
