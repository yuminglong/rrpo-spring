package com.jiebao.platfrom.check.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jiebao.platfrom.check.domain.Menus;
import com.jiebao.platfrom.check.dao.MenusMapper;
import com.jiebao.platfrom.check.service.IMenusService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.system.domain.Menu;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author qta
 * @since 2020-07-28
 */
@Service
public class MenusServiceImpl extends ServiceImpl<MenusMapper, Menus> implements IMenusService {

    @Override
    public JiebaoResponse addOrUpdate(Menus menus) {
        QueryWrapper<Menus> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("content", menus.getContent());
        if (menus.getMenusId() == null) {
            if (getOne(queryWrapper) != null) {
                return new JiebaoResponse().message("数据库内容重复");
            }
        } else {
            Menus menus1 = getById(menus.getMenusId());  //通过id查询已存在
            if (!menus1.getContent().equals(menus.getContent())) { //当本对象内容被修改时
                if (getOne(queryWrapper) != null) {
                    return new JiebaoResponse().message("数据库内容重复");
                }
            }
        }
        if (menus.getMenusId() == null) {
            menus.setDate(new Date());
        }
//        if (menus.getParentId().equals("主级")) {  //顶级菜单
//            QueryWrapper<Menus> queryWrapper = new QueryWrapper<>();
//            queryWrapper.eq("parent_id", menus.getParentId());
//            List<Menus> list = list(queryWrapper);
//            int grade = 0;
//            for (Menus menus1 : list) {
//                grade += menus1.getGrade();
//            }
//            if (grade != 100) {
//                return new JiebaoResponse().message("请检查基础工作和工作效果模块 分数总和不等于100");
//            }
//        }
        return new JiebaoResponse().message(super.saveOrUpdate(menus) ? "操作成功" : "操作失败");
    }

    @Override
    public JiebaoResponse deleteById(String menusId) {
        Menus menus = getById(menusId);
        if (menus.getParentId().equals("主级")) {
            QueryWrapper<Menus> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("parent_id", menus.getMenusId());
            remove(queryWrapper);
        }
        return new JiebaoResponse().message(removeById(menusId) ? "删除成功" : "删除成功");
    }

    @Override
    public JiebaoResponse lists() {
        List<Menus> objects = new ArrayList<>();
        QueryWrapper<Menus> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", "主级");
        List<Menus> list = list(queryWrapper);
        recursion(list);
        return new JiebaoResponse().data(list).message("操作成功");
    }

    private void recursion(List<Menus> menusList) {
        if (menusList.size() == 0) {
            return;
        }
        for (Menus menus : menusList
        ) {
            QueryWrapper<Menus> queryWrapper = new QueryWrapper<>();
            queryWrapper.orderByDesc("date");
            queryWrapper.eq("parent_id", menus.getMenusId());
            List<Menus> list = list(queryWrapper);
            menus.setChildMenus(list);
            recursion(list);
        }
    }
}
