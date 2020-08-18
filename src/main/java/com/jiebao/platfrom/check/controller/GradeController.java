package com.jiebao.platfrom.check.controller;


import com.jiebao.platfrom.check.service.IGradeService;
import com.jiebao.platfrom.common.annotation.Log;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Date;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author qta
 * @since 2020-07-28
 */
@RestController
@RequestMapping("/check/grade")
@Api(tags = "check-人员对象 扣分项绑定")
public class GradeController {
    @Autowired
    IGradeService iGradeService;
//    JiebaoResponse addGrade(String menusId, double number);
//
//    JiebaoResponse commit();//最后提交  分数统计生成表
//
//    JiebaoResponse selectByUserIdOrDateYear(Date dateYear, String userId);

    @GetMapping("selectByUserIdOrDateYear")
    @ApiOperation("获得对应人员 对应年份 细节数据")
    @Log("获得对应人员 对应年份 细节数据")
    public JiebaoResponse selectByUserIdOrDateYear(String yearId, String deptId) {
        return iGradeService.selectByUserIdOrDateYear(yearId, deptId);
    }

    @PostMapping("addGrade")
    @ApiOperation("绑定关系")
    @Log("绑定关系")
    public JiebaoResponse addGrade(String menusId, Double number, String yearId, String deptId, Double fpNumber, String message, String fpMessage) {
        return iGradeService.addGrade(menusId, number, yearId, deptId, fpNumber, message, fpMessage);
    }

    @PostMapping("addZz")
    @ApiOperation("佐证上传")
    @Log("佐证上传  参数 gradeid  类型 目标id")
    public JiebaoResponse addZz(String yearId, String deptId, String menusId, String[] filedS, String[] xXhd, String[] ySyj, String[] tZgg, String[] gGxx) {
        return iGradeService.putZz(yearId, deptId, menusId, filedS, xXhd, ySyj, tZgg, gGxx);
    }

    @GetMapping("commit")
    @ApiOperation("最后提交  分数统计生成表")
    @Log("最后提交  分数统计生成表")
    public JiebaoResponse commit(String yearId, String deptId) {
        return iGradeService.commit(yearId, deptId);
    }

    @PostMapping("checkStatus")
    @ApiOperation("疑点标记")
    @Log("疑点标记")
    public JiebaoResponse checkStatus(String gradeId, String[] zzId, String[] fileId, Integer status) {

        return iGradeService.checkStatus(gradeId, zzId, fileId, status);
    }
}
