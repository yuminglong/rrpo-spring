package com.jiebao.platfrom.check.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiebao.platfrom.check.dao.MenusMapper;
import com.jiebao.platfrom.check.dao.MenusYearMapper;
import com.jiebao.platfrom.check.domain.Grade;
import com.jiebao.platfrom.check.domain.Menus;
import com.jiebao.platfrom.check.domain.MenusYear;
import com.jiebao.platfrom.check.domain.Year;
import com.jiebao.platfrom.check.dao.YearMapper;
import com.jiebao.platfrom.check.service.IGradeService;
import com.jiebao.platfrom.check.service.IMenusService;
import com.jiebao.platfrom.check.service.IMenusYearService;
import com.jiebao.platfrom.check.service.IYearService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.domain.QueryRequest;
import com.jiebao.platfrom.system.domain.Dept;
import com.jiebao.platfrom.system.domain.Menu;
import com.jiebao.platfrom.system.service.DeptService;
import com.jiebao.platfrom.system.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author qta
 * @since 2020-08-05
 */
@Service
public class YearServiceImpl extends ServiceImpl<YearMapper, Year> implements IYearService {
    @Autowired
    MenusYearMapper menusYearMapper;
    @Autowired
    DeptService deptService;
    @Autowired
    IMenusService menusService;

    @Autowired
    IGradeService gradeService;

    @Override
    public JiebaoResponse addOrUpdate(Year year) {
        QueryWrapper<Year> queryWrapper = new QueryWrapper<>();
        if (year.getYearId() != null) {
            Year year1 = getById(year.getYearId());  //还未修改的 数据
            if (year1.getYearDate().equals(year.getYearDate())) {   //本次修改并未修改名字
                return new JiebaoResponse().message(super.saveOrUpdate(year) ? "操作成功" : "操作失败");
            }
        }
        queryWrapper.eq("year_date", year.getYearDate());
        if (getOne(queryWrapper) != null) {
            return new JiebaoResponse().message("年份重复");
        }
        return new JiebaoResponse().message(super.saveOrUpdate(year) ? "操作成功" : "操作失败");
    }

    @Override
    public List<Year> list(QueryWrapper<Year> queryWrapper) {
        List<Year> list = this.baseMapper.list(queryWrapper);
        for (Year year : list
        ) {
            List<Menus> list1 = menusService.list();  //类型分组
            HashMap<String, Integer> map = new HashMap<>();
            for (Menus menu : list1
            ) {
                map.put(menu.getEnglish(), menusYearMapper.countNumber(year.getYearId(), menu.getStandardId()));
            }
            year.setMap(map);
        }
        return list;
    }

    @Override
    public JiebaoResponse ok(String yearId) {
        List<Grade> gradeArrayList = new ArrayList<>();
        QueryWrapper<MenusYear> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("year_id", yearId);
        List<MenusYear> menusYearList = menusYearMapper.selectList(queryWrapper); //当年所有的试题
        List<Dept> childrenList = deptService.getChildrenList("0");
        for (Dept dept : childrenList
        ) {
            if (!dept.getDeptName().contains("公安处")) {
                for (MenusYear m : menusYearList
                ) {
                    //判断是否绑定已经  没写
                    Grade grade = new Grade();
                    grade.setYearId(yearId);
                    grade.setDeptId(dept.getDeptId());
                    grade.setCheckId(m.getMenusYearId());
                    gradeArrayList.add(grade);
                }
            }
        }
        JiebaoResponse jiebaoResponse = new JiebaoResponse();
        jiebaoResponse = gradeService.saveBatch(gradeArrayList) ? jiebaoResponse.okMessage("操作成功") : jiebaoResponse.failMessage("操作失败");
        return jiebaoResponse;
    }

    @Override
    public JiebaoResponse pageList(QueryRequest queryRequest, String yearDate) {
        QueryWrapper<Year> queryWrapper = new QueryWrapper<>();
        if (yearDate != null) {
            queryWrapper.eq("year_date", yearDate);
        }
        queryWrapper.orderByDesc("year_date");
        Page<Year> page = new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize());
        return new JiebaoResponse().data(this.baseMapper.pageYear(page, queryWrapper)).message("查询成功");
    }

    @Override
    public JiebaoResponse yearStringList() {
        return new JiebaoResponse().data(this.baseMapper.yearStringList()).message("查询成功");
    }


}
