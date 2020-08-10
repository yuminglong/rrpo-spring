package com.jiebao.platfrom.attendance.controller;


import com.jiebao.platfrom.attendance.daomain.Rule;
import com.jiebao.platfrom.attendance.service.IRuleService;
import com.jiebao.platfrom.common.annotation.Log;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.domain.QueryRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author qta
 * @since 2020-08-07
 */
@RestController
@RequestMapping("/attendance/rule")
@Api(tags = "attendance-rule")
public class RuleController {
    @Autowired
    IRuleService ruleService;

    @PostMapping("addOrUpdate")
    @ApiOperation("添加修改考情规则")
    @Log("添加修改考情规则")
    public JiebaoResponse addOrUpdate(Rule rule) {
        return null;
    }

    @DeleteMapping("delete/{ids}")
    @ApiOperation(("批量删除考勤规则"))
    @Log("批量删除考勤规则")
    public JiebaoResponse deletes(@PathVariable String[] ids) {
        return new JiebaoResponse().message(ruleService.removeByIds(Arrays.asList(ids)) ? "删除成功" : "删除失败");
    }

    @GetMapping("pageList")
    @ApiOperation("通过名字 模糊查询 分页  集合  考勤规则")
    @Log("通过名字 模糊查询 分页  集合  考勤规则")
    public JiebaoResponse pageList(QueryRequest queryRequest, String name) {
        return null;
    }

    @GetMapping("query")
    @ApiOperation("通过id获取考勤")
    @Log("通过id获取考勤")
    public JiebaoResponse query(String ruleId) {
        return new JiebaoResponse().data(ruleService.getById(ruleId)).message("查询成功");
    }
}
