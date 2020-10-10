package com.jiebao.platfrom.check.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jiebao.platfrom.check.dao.MenusMapper;
import com.jiebao.platfrom.check.dao.YearBindMenusMapper;
import com.jiebao.platfrom.check.dao.YearMapper;
import com.jiebao.platfrom.check.domain.Menus;
import com.jiebao.platfrom.check.domain.MenusYear;
import com.jiebao.platfrom.check.dao.MenusYearMapper;
import com.jiebao.platfrom.check.domain.Year;
import com.jiebao.platfrom.check.domain.YearZu;
import com.jiebao.platfrom.check.service.IMenusService;
import com.jiebao.platfrom.check.service.IMenusYearService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiebao.platfrom.check.service.IYearBindMenusService;
import com.jiebao.platfrom.check.service.IYearService;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.utils.CheckExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author qta
 * @since 2020-08-05
 */
@Service
public class MenusYearServiceImpl extends ServiceImpl<MenusYearMapper, MenusYear> implements IMenusYearService {

    @Autowired
    MenusMapper menusMapper;
    @Autowired
    IMenusService menusService;
    @Autowired
    IYearService yearService;
    @Autowired
    MenusYearMapper menusYearMapper;
    @Autowired
    YearBindMenusMapper yearBindMenusMapper;
    @Autowired
    YearMapper yearMapper;


    @Override
    public boolean saveOrUpdate(MenusYear entity) {


        return super.saveOrUpdate(entity);
    }

    @Override
    public JiebaoResponse addOrUpdate(MenusYear menusYear) {
        JiebaoResponse jiebaoResponse = new JiebaoResponse();
        if (menusYear.getMenusYearId() == null) {
            Integer integer = this.baseMapper.exSit(menusYear.getContent(), menusYear.getYearId());
            if (integer != null) {
                jiebaoResponse.failMessage("内容重复");
            }
            menusYear.setDate(new Date());
        } else {
            MenusYear menusYear1 = getById(menusYear.getMenusYearId()); //数据库已存在的
            if (!menusYear1.getContent().equals(menusYear.getContent())) {  //如果内容发生了变化
                Integer integer = this.baseMapper.exSit(menusYear.getContent(), menusYear.getYearId());
                if (integer != null) {
                    jiebaoResponse.failMessage("内容重复");
                }
            }
        }
        jiebaoResponse = saveOrUpdate(menusYear) ? jiebaoResponse.okMessage("操作成功") : jiebaoResponse.failMessage("操作失败");
        return jiebaoResponse;
    }

    @Override
    public JiebaoResponse List(String yearId) {  //通过 年份考核 查询 对应的年内考核项
        List<String> menusId = yearBindMenusMapper.listMenusId(yearId);
        List<YearZu> list = new ArrayList<>();
        for (String menuId : menusId  //考核分组类型
        ) {
            Menus menus = menusService.getById(menuId);
            if (menus == null) {
                continue;
            }
            QueryWrapper<MenusYear> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("year_id", yearId);
            queryWrapper.eq("parent_id", menus.getStandardId());
            YearZu yearZu = new YearZu();
            yearZu.setId(menus.getStandardId());
            yearZu.setName(menus.getName());
            yearZu.setNum(menus.getNum());
            yearZu.setList(this.baseMapper.selectList(queryWrapper));
            list.add(yearZu);
        }
        return new JiebaoResponse().data(list).message("查询成功");
    }

    @Override
    public JiebaoResponse deleteByListAndYearDate(String[] list) {
        if (list == null) {
            return new JiebaoResponse().message("空");
        }
        return new JiebaoResponse().message(removeByIds(Arrays.asList(list)) ? "删除成功" : "删除失败");
    }

    @Override
    public JiebaoResponse excel(MultipartFile multipartFile, String year_id) {
        return CheckExcelUtil.excel(year_id, multipartFile, menusService, this, menusYearMapper, yearBindMenusMapper);
    }

    public static void main(String[] args) {
        Calendar instance = Calendar.getInstance();
        System.out.println(instance.get(Calendar.MONTH));
        System.out.println(instance.get(Calendar.YEAR));
    }

}
