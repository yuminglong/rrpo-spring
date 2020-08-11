package com.jiebao.platfrom.check.controller;


import com.jiebao.platfrom.check.domain.Year;
import com.jiebao.platfrom.check.service.IMenusYearService;
import com.jiebao.platfrom.common.annotation.Log;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author qta
 * @since 2020-08-05
 */
@RestController
@RequestMapping("/check/menus-year")
@Api(tags = "check_年份考核与考核项绑定")
public class MenusYearController {

    @Autowired
    IMenusYearService menusYearService;

    @PostMapping("add")
    @ApiOperation("添加  考核年份规则生成")
    @Log("添加  考核年份规则生成")
    public JiebaoResponse add(String yearID,@RequestParam("menusId") List<String> menusId) {
        return menusYearService.add(yearID, menusId);
    }

    @GetMapping("list")
    @ApiOperation("集合")
    @Log("查询年份与考核项挂钩")
    public JiebaoResponse list(String yearId,String yearDate) {
        return new JiebaoResponse().data(menusYearService.List(yearId,yearDate)).message("查询成功");
    }

    @DeleteMapping("deleteByLists/{listS}")
    @ApiOperation("集合删除")
    @Log("年份考核项集合删除")
    public JiebaoResponse deleteByLists(@PathVariable String[] listS) {
        return new JiebaoResponse().message(menusYearService.removeByIds(Arrays.asList(listS)) ? "删除成功" : "删除失败");
    }
}
