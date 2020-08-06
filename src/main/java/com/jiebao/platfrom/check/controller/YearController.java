package com.jiebao.platfrom.check.controller;


import com.jiebao.platfrom.accident.daomain.Track;
import com.jiebao.platfrom.check.domain.Year;
import com.jiebao.platfrom.check.service.IYearService;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.domain.QueryRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/check/year")
@Api(tags = "check-考核年份规则生成")
public class YearController {
    @Autowired
    IYearService yearService;

    @PostMapping("saveOrUpdate")
    @ApiOperation("添加修改  考核年份规则生成")
    public JiebaoResponse saveOrUpdate(Year year) {
        return yearService.addOrUpdate(year);
    }

    @GetMapping("list")
    @ApiOperation("集合")
    public JiebaoResponse list() {
        return new JiebaoResponse().data(yearService.list()).message("查询成功");
    }

    @GetMapping("pageList")
    @ApiOperation("集合")
    public JiebaoResponse pageList(QueryRequest queryRequest, String yearDate) {
        return new JiebaoResponse().data(yearService.pageList(queryRequest, yearDate)).message("查询成功");
    }

    @DeleteMapping("deleteByLists")
    @ApiOperation("集合删除")
    public JiebaoResponse deleteByLists(@RequestParam("lists") List<String> lists) {
        return new JiebaoResponse().message(yearService.removeByIds(lists) ? "删除成功" : "删除失败");
    }

}
