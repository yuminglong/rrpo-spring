package com.jiebao.platfrom.check.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jiebao.platfrom.check.domain.Menus;
import com.jiebao.platfrom.check.service.IMenusService;
import com.jiebao.platfrom.common.annotation.Log;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author qta
 * @since 2020-07-28
 */
@RestController
@RequestMapping("/check/menus")
@Api(tags = "考核项")
public class MenusController {
    @Autowired
    IMenusService menusService;

    @PostMapping("addOrUpdate")
    @ApiOperation("添加修改考核项")
    @Log("添加修改考核项")
    public JiebaoResponse addOrUpdate(Menus menus) {
        return menusService.addOrUpdate(menus);
    }

    @GetMapping("deleteById")
    @ApiOperation("删除")
    @Log("删除")
    public JiebaoResponse deleteById(String menusId) {
        return menusService.deleteById(menusId);
    }

    @GetMapping("lists")
    @ApiOperation("获取树形列表")
    @Log("获取树形列表")
    public JiebaoResponse lists() {
        return menusService.lists();
    }
}
