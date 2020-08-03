package com.jiebao.platfrom.check.controller;


import com.jiebao.platfrom.check.service.INumService;
import com.jiebao.platfrom.common.annotation.Log;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.domain.QueryRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author qta
 * @since 20000000000000000000000000000000000000000000000000020-07-28
 */
@RestController
@RequestMapping("/check/num")
@Api(tags = "check-年终考核统计分数表")
public class NumController {
    @Autowired
    INumService numService;

    @GetMapping("")
    @ApiOperation("获得数据 年终考核 ")
    @Log("获得数据 年终考核")
    public JiebaoResponse pageList(QueryRequest queryRequest, String userName, String deptId, String dateYear) {
        return numService.pageList(queryRequest, userName, deptId, dateYear);
    }
}
