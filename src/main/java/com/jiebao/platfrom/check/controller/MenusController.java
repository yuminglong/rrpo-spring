package com.jiebao.platfrom.check.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jiebao.platfrom.check.domain.Menus;
import com.jiebao.platfrom.check.service.IMenusService;
import com.jiebao.platfrom.common.annotation.Log;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
@Api(tags = "check-考核项")
public class MenusController {
    @Autowired
    IMenusService menusService;

    @PostMapping("addOrUpdate")
    @ApiOperation("添加修改考核项")
    @Log("添加修改考核项")
    public JiebaoResponse addOrUpdate(Menus menus) {
        return menusService.addOrUpdate(menus);
    }


    @GetMapping("lists")
    @ApiOperation("获取树形列表")
    @Log("获取树形列表")
    public JiebaoResponse lists() {
        return menusService.lists();
    }

    @GetMapping("selectById")
    @ApiOperation("查询对应扣分项")
    @Log("查询对应扣分项")
    public JiebaoResponse selectById(String menusId) {
        return new JiebaoResponse().data(menusService.getById(menusId)).message("查询成功");
    }

    @DeleteMapping( "deleteById/{menusId}")
    @ApiOperation("删除")
    @Log("删除")
    public JiebaoResponse deleteById(String menusId) {
        return menusService.deleteById(menusId);
    }

    @DeleteMapping("deleteListById/{menusIdS}")
    @ApiOperation("集合删除")
    @Log("集合删除")
    public JiebaoResponse deleteListById(@RequestParam("menusIdS") List<String> menusIdS) {
        return new JiebaoResponse().message(menusService.removeByIds(menusIdS) ? "删除成功" : "删除失败");
    }
}
