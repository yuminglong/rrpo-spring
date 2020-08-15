package com.jiebao.platfrom.check.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jiebao.platfrom.check.dao.MenusMapper;
import com.jiebao.platfrom.check.domain.Menus;
import com.jiebao.platfrom.check.domain.MenusYear;
import com.jiebao.platfrom.check.dao.MenusYearMapper;
import com.jiebao.platfrom.check.domain.Year;
import com.jiebao.platfrom.check.service.IMenusService;
import com.jiebao.platfrom.check.service.IMenusYearService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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

    @Override
    public JiebaoResponse add(String yearID, List<String> menusIds) {
        QueryWrapper<MenusYear> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("year_id", yearID);
        remove(queryWrapper);
        ArrayList<MenusYear> menusYearArrayList = new ArrayList<>();
        for (String menusId : menusIds
        ) {
            MenusYear menusYear = new MenusYear();
            menusYear.setMenusId(menusId);
            menusYear.setYearId(yearID);
            menusYear.setParentId(menusService.getById(menusId).getParentId());
            menusYearArrayList.add(menusYear);
        }
        return new JiebaoResponse().message(saveBatch(menusYearArrayList) ? "绑定成功" : "绑定失败");
    }

    @Override
    public JiebaoResponse List(String yearId, String yearDate) {  //通过 年份考核 查询 对应的年内考核项
        List<String> JcMenusIdList = null;
        List<String> XgMenusIdList = null;
        if (yearId != null) {
            JcMenusIdList = this.baseMapper.getMenusIdList(yearId, menusMapper.getMenusIdByName("基础工作"));
            XgMenusIdList = this.baseMapper.getMenusIdList(yearId, menusMapper.getMenusIdByName("工作效果"));
        }
        if (yearDate != null) {
            QueryWrapper<Year> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("year_date", yearDate);
            Year year = yearService.getOne(queryWrapper);
            if (year != null) {
                JcMenusIdList = this.baseMapper.getMenusIdList(yearId, menusMapper.getMenusIdByName("基础工作"));
                XgMenusIdList = this.baseMapper.getMenusIdList(yearId, menusMapper.getMenusIdByName("工作效果"));
            }
        }
        HashMap<String, List<Menus>> map = new HashMap<>();  //最后返回
        if (JcMenusIdList != null) {
            QueryWrapper<Menus> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("menus_id", JcMenusIdList);
            map.put("JCgz", menusService.list(queryWrapper));
        }
        if (XgMenusIdList != null) {
            QueryWrapper<Menus> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("menus_id", XgMenusIdList);
            map.put("GZxg", menusService.list(queryWrapper));
        }
        return new JiebaoResponse().data(map).message("查询成功");
    }

    @Override
    public JiebaoResponse deleteByListAndYearDate(String[] list, String yearDate) {
        if (list == null) {
            return new JiebaoResponse().message("请选择选出对象");
        }
        QueryWrapper<Year> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("year_date", yearDate);
        Year year = yearService.getOne(queryWrapper);
        QueryWrapper<MenusYear> queryWrapper1 = new QueryWrapper<>();  //绑定表
        queryWrapper1.eq("year_id", year.getYearId());
        queryWrapper1.in("menus_id", Arrays.asList(list));
        return new JiebaoResponse().message(remove(queryWrapper1) ? "删除成功" : "删除失败");
    }

    @Override
    public JiebaoResponse excel(MultipartFile multipartFile, String year_id) {
        Map<String, List<String>> excel = CheckExcelUtil.excel(multipartFile);
        Date date = new Date();
        String GZxg = menusMapper.getMenusIdByName("工作效果");//对应的id
        String JCgz = menusMapper.getMenusIdByName("基础工作");
        List<Menus> list = new ArrayList<>();
        for (String JCContent : excel.get("jc")
        ) {
            Menus menus = new Menus();
            menus.setDate(date);
            menus.setContent(JCContent);
            menus.setParentId(JCgz);
            menus.setAccessory(1);
            list.add(menus);
        }
        for (String JCContent : excel.get("xg")
        ) {
            Menus menus = new Menus();
            menus.setDate(date);
            menus.setContent(JCContent);
            menus.setParentId(GZxg);
            menus.setAccessory(1);
            list.add(menus);
        }
        JiebaoResponse jiebaoResponse = new JiebaoResponse();
        jiebaoResponse = menusService.saveBatch(list) ? jiebaoResponse.okMessage("录入成功") : jiebaoResponse.failMessage("录入失败");
        return jiebaoResponse;
    }
}
