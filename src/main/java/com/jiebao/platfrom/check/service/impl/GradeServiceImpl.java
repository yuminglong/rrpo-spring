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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
    public JiebaoResponse addGrade(String menusId, Double number, String yearDate, String deptId) {  //menusId  既是 扣分项id
        QueryWrapper<Grade> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("check_id", menusId);//对应的扣分项
        queryWrapper.eq("year_date", yearDate);//年份
        queryWrapper.eq("dept_id", deptId);
//        User user = (User)SecurityUtils.getSubject().getPrincipal();  //当前登陆人
//        queryWrapper.eq(("user_id"), user.getUserId());
        Grade grade = getOne(queryWrapper);
        if (grade != null) {
            grade.setNum(number);
        } else {
            grade = new Grade();
            grade.setNum(number == null ? 0 : number);
            grade.setCheckId(menusId);
            grade.setDeptId(deptId);
            grade.setYearDate(yearDate);
        }
        return new JiebaoResponse().message(super.saveOrUpdate(grade) ? "操作成功" : "操作失败");
    }

    @Override
    public JiebaoResponse commit(String yearDate, String deptId) {   //  生成报表
        QueryWrapper<Grade> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(("dept_id"), deptId);
        queryWrapper.eq("year_date", yearDate);
        List<Grade> list = list(queryWrapper);
        QueryWrapper<Menus> queryWrapperJC = new QueryWrapper<>();  //  基础工作
        queryWrapperJC.eq("content", "基础工作");
        Menus menusJc = menusService.getOne(queryWrapperJC);//基础工作父类
        QueryWrapper<Menus> queryWrapperXG = new QueryWrapper<>();
        queryWrapperXG.eq("content", "工作效果");//效果
        Menus menusSX = menusService.getOne(queryWrapperXG);//工作失效父类
        double JCKF = 0;  //基础工作的扣分项
        double JCJF = 0;//基础工作加分项
        double SGKF = 0;//工作效果扣分项
        for (Grade grade : list) {
            Menus menus = menusService.getById(grade.getCheckId());
            if (menus.getParentId().equals(menusJc.getMenusId())) {  //此条数据对应   基础工作  扣分模块规则
                if (grade.getNum() > 0) {  //如果大于0 证明为加分项
                    JCJF += grade.getNum();
                } else {  //反之 为扣分项
                    JCKF += grade.getNum();
                }
            } else {
                SGKF += grade.getNum();
            }
        }
        SGKF = 40 + SGKF;
        SGKF = SGKF < 0 ? 0 : SGKF;
        JCKF = JCKF < -20 ? -20 : JCKF;  //扣分  20为限
        if (JCJF > 40) {
            double dx = (JCJF - 40) / 15;  //溢出部分抵消扣分
            JCKF = JCKF + dx;  //抵消后的分数
            if (JCKF > 0) {
                JCKF = 0;
            }
            JCJF = 40;  //40分为限  超过40分  折价  15：1
        }
        QueryWrapper<Num> numQueryWrapper = new QueryWrapper<>();
        numQueryWrapper.eq("year_date", yearDate);
        numQueryWrapper.eq("dept_id", deptId);
        Num num = numService.getOne(numQueryWrapper);  //年度考核表储存对象
        if (num == null) {
            num = new Num();  //为空创造对象
        }
        num.setDeptId(deptId);
        num.setJcWork(JCKF + JCJF);
        num.setXgWork(SGKF);
        num.setYearDate(yearDate);
        num.setNumber(20 + JCKF + JCJF + SGKF);
        return new JiebaoResponse().message(numService.saveOrUpdate(num) ? "操作成功" : "操作失败");
    }

    @Override
    public JiebaoResponse selectByUserIdOrDateYear(String dateYear, String DeptId) {  //必填 时间   对象id
        QueryWrapper<Grade> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("year_date", dateYear);
        queryWrapper.eq("dept_id", DeptId);
        List<Grade> list = list(queryWrapper);   //目标人  关联扣分项
        for (Grade g : list
        ) {
            System.out.println(g);
        }
        QueryWrapper<Menus> queryWrapperJC = new QueryWrapper<>();  //  基础工作
        queryWrapperJC.eq("content", "基础工作");
        Menus menusJc = menusService.getOne(queryWrapperJC);//基础工作父类
        QueryWrapper<Menus> queryWrapperXG = new QueryWrapper<>();
        queryWrapperXG.eq("content", "工作效果");//效果
        Menus menusSX = menusService.getOne(queryWrapperXG);//工作失效父类
        List<Menus> jcList = new ArrayList<>(); //储存基础map
        List<Menus> xgList = new ArrayList<>();   //储存工作实效map
        HashMap<String, List<Menus>> map = new HashMap<>();   //总map
        for (Grade grade : list) {
            Menus menus = menusService.getById(grade.getCheckId());
            menus.setNumber(grade.getNum());
            if (menus.getParentId().equals(menusJc.getMenusId())) {  //基础工作模块
                jcList.add(menus);
            } else {  //工作效果模块
                xgList.add(menus);
            }
        }
        map.put("JCgz", jcList);
        map.put("GZxg", xgList);
        return new JiebaoResponse().data(map).message("操作成功");
    }

}
