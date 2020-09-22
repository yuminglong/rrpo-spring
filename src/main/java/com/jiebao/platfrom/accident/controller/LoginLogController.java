package com.jiebao.platfrom.accident.controller;


import com.jiebao.platfrom.common.annotation.Log;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.system.service.LoginLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/accident/login")
@Api(tags = "accident-用户登录次数统计分析")
public class LoginLogController {
    @Autowired
    LoginLogService loginLogService;

    @PostMapping("list")
    @ApiOperation("查看登录记录")
    @Log("查看登录记录")
    public JiebaoResponse list(String deptParentId, Date startDate, Date endDate) {
        return loginLogService.lists(deptParentId, startDate, endDate);
    }
}
