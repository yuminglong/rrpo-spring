package com.jiebao.platfrom.attendance.controller;


import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.domain.QueryRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
 * @since 2020-08-07
 */
@RestController
@RequestMapping("/attendance/record")
@Api(tags = "attendance-record")
public class RecordsController {

    @PostMapping("punch")
    @ApiOperation("打卡接口")
    public JiebaoResponse punch() {
        return null;
    }


    @GetMapping("listPage")
    @ApiOperation("打卡记录查询  部门  姓名模糊 时间")
    public JiebaoResponse list(QueryRequest queryRequest, String deptId, String name, String date) {
        return null;
    }
}
