package com.jiebao.platfrom.check.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jiebao.platfrom.check.dao.MenusMapper;
import com.jiebao.platfrom.check.domain.Menus;
import com.jiebao.platfrom.check.domain.MenusYear;
import com.jiebao.platfrom.check.dao.MenusYearMapper;
import com.jiebao.platfrom.check.service.IMenusService;
import com.jiebao.platfrom.check.service.IMenusYearService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
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
public class MenusYearServiceImpl extends ServiceImpl<MenusYearMapper, MenusYear> implements IMenusYearService {

    @Autowired
    MenusMapper menusMapper;
    @Autowired
    IMenusService menusService;

    @Override
    public JiebaoResponse add(String yearID, List<String> menusIds) {
        QueryWrapper<MenusYear> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("menus_id", menusIds);
        queryWrapper.eq("year_id", yearID);
        List<MenusYear> list = list(queryWrapper);
        for (MenusYear menusYear : list
        ) {
            if (menusIds.contains(menusYear.getMenusId())) {//如果存在
                menusIds.remove(menusYear.getMenusId());  //删除
            }
        }
        ArrayList<MenusYear> menusYearArrayList = new ArrayList<>();
        for (String menusId : menusIds
        ) {
            MenusYear menusYear = new MenusYear();
            menusYear.setMenusId(menusId);
            menusYear.setYearId(yearID);
            menusYearArrayList.add(menusYear);
        }
        return new JiebaoResponse().message(saveBatch(menusYearArrayList) ? "绑定成功" : "绑定失败");
    }

    @Override
    public JiebaoResponse List(String yearId, String yearDate) {  //通过 年份考核 查询 对应的年内考核项
        List<String> menusIdList = null;
        if (yearId != null) {
            menusIdList = this.baseMapper.getMenusIdList(yearId);
        }
        if (yearDate != null) {
            QueryWrapper<MenusYear> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("year_date", yearDate);
            MenusYear one = getOne(queryWrapper);
            if (one != null) {
                menusIdList = this.baseMapper.getMenusIdList(one.getYearId());
            }
        }
        if (menusIdList.size() == 0) {
            new JiebaoResponse().message("本规则还未绑定任何扣分项");
        }
        QueryWrapper<Menus> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("menus_id", menusIdList);
        List<Menus> list = menusService.list(queryWrapper);
        String GZxg = menusMapper.getMenusIdByName("工作效果");
        String JCgz = menusMapper.getMenusIdByName("基础工作");
        List<Menus> JCgzList = new ArrayList<>();
        List<Menus> GZxgList = new ArrayList<>();
        HashMap<String, List<Menus>> map = new HashMap<>();  //最后返回
        for (Menus menus : list
        ) {
            if (menus.getParentId().equals(JCgz)) {
                //基础工作项
                JCgzList.add(menus);

            }

            if (menus.getParentId().equals(GZxg)) {  //工作效果项
                GZxgList.add(menus);
            }
        }
        map.put("JCgz", JCgzList);
        map.put("GZxg", GZxgList);
        return new JiebaoResponse().data(map).message("查询成功");
    }

}
