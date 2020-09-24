package com.jiebao.platfrom.accident.controller;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.jiebao.platfrom.common.annotation.Log;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.system.service.LoginLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/accident/login")
@Api(tags = "accident-用户登录次数统计分析")
public class LoginLogController {
    @Autowired
    LoginLogService loginLogService;


    @PostMapping("list")
    @ApiOperation("查看登录记录")
    @Log("查看登录记录")
    public JiebaoResponse list(String deptParentId, String startDate, String endDate) {
        return loginLogService.lists(deptParentId, startDate, endDate);
    }

    @PostMapping("userList")
    @ApiOperation("查询组织 具体人员登录次数")
    @Log("查询组织 具体人员登录次数")
    public JiebaoResponse userList(String deptId, String startDate, String endDate) {
        return loginLogService.listUsers(deptId, startDate, endDate);
    }
}
