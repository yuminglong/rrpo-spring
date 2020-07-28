package com.jiebao.platfrom.check.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jiebao.platfrom.check.domain.Grade;
import com.jiebao.platfrom.check.dao.GradeMapper;
import com.jiebao.platfrom.check.domain.Menus;
import com.jiebao.platfrom.check.domain.Num;
import com.jiebao.platfrom.check.service.IGradeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiebao.platfrom.check.service.IMenusService;
import com.jiebao.platfrom.check.service.INumService;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.system.domain.User;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.SimpleFormatter;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author qta
 * @since 2020-07-28
 */
@Service
public class GradeServiceImpl extends ServiceImpl<GradeMapper, Grade> implements IGradeService {
    @Autowired
    IMenusService menusService;

    @Autowired
    INumService numService;

    @Override
    public JiebaoResponse addGrade(String menusId, double number) {  //menusId  既是 扣分项id
        Date date = new Date();
        SimpleDateFormat simpleFormatter = new SimpleDateFormat("yyyy");
        String formatYear = simpleFormatter.format(date);
        QueryWrapper<Grade> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("check_id", menusId);//对应的扣分项
        queryWrapper.eq("year_date", formatYear);//年份
//        User user = (User)SecurityUtils.getSubject().getPrincipal();  //当前登陆人
//        queryWrapper.eq(("user_id"), user.getUserId());
        Grade grade = getOne(queryWrapper);
        if (grade != null) {
            grade.setNum(number);
        } else {
            grade = new Grade();
            grade.setNum(number);
            grade.setCheckId(menusId);
            //   one.setUserId(user.getUserId());
        }
        return new JiebaoResponse().message(super.saveOrUpdate(grade) ? "操作成功" : "操作失败");
    }

    @Override
    public JiebaoResponse commit() {
        User user = (User) SecurityUtils.getSubject().getPrincipal();  //当前登陆人
        Date date = new Date();
        SimpleDateFormat simpleFormatter = new SimpleDateFormat("yyyy");
        String formatYear = simpleFormatter.format(date);
        QueryWrapper<Grade> queryWrapper = new QueryWrapper<>();
        // queryWrapper.eq(("user_id"), user.getUserId());
        //  queryWrapper.eq("user_id",user.getUserId());
        queryWrapper.eq("year_date", formatYear);
        List<Grade> list = list(queryWrapper);
        QueryWrapper<Menus> queryWrapper1 = new QueryWrapper<>();
        queryWrapper.eq("name", "基础工作");
        Menus menusJc = menusService.getOne(queryWrapper1);//基础工作父类
        queryWrapper1 = new QueryWrapper<>();
        queryWrapper.eq("name", "工作失效");
        Menus menusSX = menusService.getOne(queryWrapper1);//工作失效父类
        double JCKF = 0;  //基础工作的扣分项
        double JCJF = 0;//基础工作加分项
        double SXKF = 0;//工作失效扣分项
        for (Grade grade : list) {
            Menus menus = menusService.getById(grade.getCheckId());
            if (menus.getParentId().equals(menusJc.getMenusId())) {  //此条数据对应   基础工作  扣分模块规则
                if (grade.getNum() > 0) {  //如果大于0 证明为加分项
                    JCJF += grade.getNum();
                } else {  //反之 为扣分项
                    JCKF += grade.getNum();
                }
            } else {
                SXKF += grade.getNum();
            }
        }
        JCKF = JCKF < -20 ? -20 : JCKF;  //扣分  20为限
        if (JCJF > 40) {
            JCJF = JCJF + (JCJF - 40) / 15;  //40分为限  超过40分  折价  15：1
        }
        QueryWrapper<Num> numQueryWrapper = new QueryWrapper<>();
        numQueryWrapper.eq("year_date", formatYear);
        numQueryWrapper.eq("user_id", user.getUserId());
        Num num = numService.getOne(numQueryWrapper);  //年度考核表储存对象
        if (num == null) {
            num = new Num();  //为空创造对象
        }
        num.setDeptId(user.getUserId());
        num.setDeptId(user.getDeptId());
        num.setYearDate(formatYear);
        num.setNumber(20 + JCKF + JCJF + SXKF);
        return new JiebaoResponse().message(numService.saveOrUpdate(num) ? "操作成功" : "操作失败");
    }


}
