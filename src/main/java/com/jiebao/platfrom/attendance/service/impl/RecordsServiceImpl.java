package com.jiebao.platfrom.attendance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiebao.platfrom.attendance.dao.RuleMapper;
import com.jiebao.platfrom.attendance.daomain.Record;
import com.jiebao.platfrom.attendance.dao.RecordsMapper;
import com.jiebao.platfrom.attendance.daomain.Rule;
import com.jiebao.platfrom.attendance.daomain.Statement;
import com.jiebao.platfrom.attendance.service.IRecordsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiebao.platfrom.attendance.service.IRuleService;
import com.jiebao.platfrom.attendance.service.IStatementService;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.domain.QueryRequest;
import com.jiebao.platfrom.system.domain.User;
import com.jiebao.platfrom.system.service.DeptService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author qta
 * @since 2020-08-07
 */
@Service
public class RecordsServiceImpl extends ServiceImpl<RecordsMapper, Record> implements IRecordsService {
    @Autowired
    DeptService deptService;
    @Autowired
    IRuleService ruleService;
    @Autowired
    RuleMapper ruleMapper;
    @Autowired
    IStatementService statementService;

    public JiebaoResponse punch(Integer ifLate) {
        Rule rule = ruleMapper.getRuleByUsing();  //当前启用的考勤规则
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String format = simpleDateFormat.format(date); //天数日期
        QueryWrapper<Record> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("date_day", format);
        queryWrapper.eq("user_id", user.getUserId());
        QueryWrapper<Statement> statementQueryWrapper = new QueryWrapper<>();
        queryWrapper.eq("date_day", format);
        queryWrapper.eq("user_id", user.getUserId());
        Statement statement = statementService.getOne(statementQueryWrapper);  //具体的统计信息
        Record record = getOne(queryWrapper);
        boolean qk = false;
        if (record == null) {
            qk = true;   //未有记录
            record = new Record();
            record.setName(user.getUsername());
            record.setUserId(user.getUserId());
            record.setDeptId(user.getDeptId());
            String deptName = deptService.getById(user.getDeptId()).getDeptName();
            record.setDept(deptName);
            record.setDateDay(format);
            statement = new Statement();//
            statement.setDeptId(user.getDeptId());
            statement.setDeptName(deptName);
            statement.setUserName(user.getUsername());
            statement.setUserId(user.getUserId());
            statement.setDateDay(format);
        }
        if (ifLate == 0) {   //上班操作
            record.setStartDate(date);
            Integer count = count(rule.getStartDate(), date);
            statement.setLate(count > 0 ? count : 0); //当前时间分钟数 比规定大   则为迟到
            statement.setStartDate(date);
        } else {//下班操作
            if (qk) {  //今日未打卡 且直接触发下班打卡   则补齐相关数据
                statement.setDummy(1);  //缺卡
            }
            record.setEndDate(date);
            statement.setEndDate(date);
            Integer count = count(rule.getEndDate(), date);
            statement.setLeave(count < 0 ? count * -1 : 0); //迟到分钟
        }
        statementService.saveOrUpdate(statement);
        super.saveOrUpdate(record);
        return new JiebaoResponse().message(ifLate == 0 ? "上班打卡成功" : "下班打卡成功");
    }

    @Override
    public JiebaoResponse list(QueryRequest queryRequest, String deptId, String name, String date) {
        QueryWrapper<Record> queryWrapper = new QueryWrapper<>();
        if (deptId != null) {
            queryWrapper.eq("dept_id", deptId);
        }
        if (name != null) {
            queryWrapper.like("name", name);
        }
        if (date != null) {
            queryWrapper.eq("date_day", date);
        }
        queryWrapper.orderByDesc("date");
        Page<Record> page = new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize());
        return new JiebaoResponse().data(page(page, queryWrapper)).message("查询成功");
    }

    @Override
    public JiebaoResponse selectIfLate() {
        Date date = new Date();
        Rule rule = ruleMapper.getRuleByUsing();  //当前启用的考勤规则
        String startDate = rule.getStartDate();   //上班时间
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("HH:mm");
        Date starDates = null;
        Date parse = null;
        try {
            starDates = simpleDateFormat1.parse(startDate);  //得到上班时间转换为  特定格式
            parse = simpleDateFormat1.parse(String.valueOf(date));  //今天的小时时间
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int ifLate = parse.getTime() - starDates.getTime() > rule.getMaxLate() * 60 * 1000 ? 1 : 0;
        return new JiebaoResponse().data(ifLate).message(ifLate == 0 ? "上班打卡" : "下班打卡");
    }

    private Integer count(String dateForMA, Date date) {  //传入时间与当前时间的时间差值
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("HH:mm");
        Date starDates = null;
        Date parse = null;
        try {
            starDates = simpleDateFormat1.parse(dateForMA);  //得到上班时间转换为  特定格式
            parse = simpleDateFormat1.parse(String.valueOf(date));  //今天的小时时间
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Long l = (parse.getTime() - starDates.getTime()) / 1000 / 60;
        return Integer.parseInt(l.toString());
    }
}
