package com.jiebao.platfrom.check.controller;


import com.jiebao.platfrom.check.service.IGradeZzService;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
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
 * @since 2020-08-11
 */
@RestController
@RequestMapping("/check/grade-zz")
@Api(tags = "check-佐证查询")
public class GradeZzController {
    @Autowired
    IGradeZzService gradeZzService;

    @GetMapping("list")
    @ApiOperation("佐证查询  参数每条扣分记录id")
    public JiebaoResponse list(String gradeId, String yearDate, String deptId, String menusId) {
        return gradeZzService.list(gradeId, yearDate, deptId, menusId);
    }

    @DeleteMapping("delete/{list}")
    @ApiOperation("删除佐证")
    public JiebaoResponse delete(@PathVariable String[] list) {
        return new JiebaoResponse().message(gradeZzService.removeByIds(Arrays.asList(list)) ? "操作成功" : "操作失败");
    }

}
