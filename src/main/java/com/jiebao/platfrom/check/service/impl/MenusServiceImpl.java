package com.jiebao.platfrom.check.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiebao.platfrom.check.domain.Menus;
import com.jiebao.platfrom.check.dao.MenusMapper;
import com.jiebao.platfrom.check.service.IMenusService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.domain.QueryRequest;
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
        menus.setDate(new Date());
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
    public JiebaoResponse lists(QueryRequest queryRequest, String menusId) {
        List<Menus> objects = new ArrayList<>();
        QueryWrapper<Menus> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", menusId);
        Page<Menus> page = new Page<>();
        return new JiebaoResponse().data(page(page, queryWrapper)).message("操作成功");
    }

    @Override
    public JiebaoResponse fatherList() {
        QueryWrapper<Menus> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", "主级");
        return new JiebaoResponse().data(list(queryWrapper)).message("查询成功");
    }

}
