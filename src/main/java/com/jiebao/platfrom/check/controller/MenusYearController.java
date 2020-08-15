package com.jiebao.platfrom.check.controller;


import com.jiebao.platfrom.check.domain.Year;
import com.jiebao.platfrom.check.service.IMenusYearService;
import com.jiebao.platfrom.common.annotation.Log;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.utils.CheckExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
    public JiebaoResponse add(String yearID, @RequestParam("menusId") List<String> menusId) {
        return menusYearService.add(yearID, menusId);
    }

    @GetMapping("list")
    @ApiOperation("集合")
    @Log("查询年份与考核项挂钩")
    public JiebaoResponse list(String yearId, String yearDate) {
        return new JiebaoResponse().data(menusYearService.List(yearId, yearDate)).message("查询成功");
    }

    @DeleteMapping("deleteByLists")
    @ApiOperation("集合删除")
    @Log("年份考核项集合删除")
    public JiebaoResponse deleteByLists(String[] listS, String yearDate) {
        return menusYearService.deleteByListAndYearDate(listS, yearDate);
    }

    @PostMapping("excel")
    @ApiOperation("excel上传绑定")
    public JiebaoResponse excel(MultipartFile multipartFile) {
        Map<String, List<String>> excel = CheckExcelUtil.excel(multipartFile);
        return new JiebaoResponse().data(excel);
    }
}
