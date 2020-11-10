package com.jiebao.platfrom.accident.controller;


import com.jiebao.platfrom.accident.daomain.Accident;
import com.jiebao.platfrom.accident.daomain.Case;
import com.jiebao.platfrom.accident.service.ICaseService;
import com.jiebao.platfrom.common.annotation.Log;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.domain.QueryRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
@RequestMapping("/accident/case")
@Api(tags = "accident-设铁案")
public class CaseController {
    @Autowired
    ICaseService caseService;

    @PostMapping("saveOrUpdate")
    @ApiOperation("添加修改 事故信息")
    @Log("添加修改 事故信息")
    public JiebaoResponse saveOrUpdate(Case ca) {
        JiebaoResponse jiebaoResponse = new JiebaoResponse();
        if (ca.getCaseId() != null) {
            Case aCase = caseService.getById(ca.getCaseId());
            if (aCase.getStatu() != null && aCase.getStatu() == 1)
                return jiebaoResponse.failMessage("已锁定不可更改");
        } else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
            try {
                ca.setMonth(simpleDateFormat.format(simpleDateFormat.parse(ca.getDate())));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        jiebaoResponse = caseService.saveOrUpdate(ca) ? jiebaoResponse.okMessage("操作成功") : jiebaoResponse.failMessage("操作失败");
        return jiebaoResponse;
    }

    @DeleteMapping("deleteByLists/{lists}")
    @ApiOperation("集合删除事故信息")
    @Log("集合删除事故信息")
    public JiebaoResponse deleteByLists(@PathVariable String[] lists) {
        JiebaoResponse jiebaoResponse = new JiebaoResponse();
        jiebaoResponse = caseService.removeByIds(Arrays.asList(lists)) ? jiebaoResponse.okMessage("删除成功") : jiebaoResponse.failMessage("删除失败");
        return jiebaoResponse;
    }

    @GetMapping("listPage")
    @ApiOperation("条件查询分页  事故信息")
    @Log("条件查询分页  事故信息")
    public JiebaoResponse list(QueryRequest queryRequest, String policeId, String cityLevelId,String quDeptId, String lineId, String nature, String startDate, String endDate) {
        return caseService.list(queryRequest, policeId, cityLevelId,quDeptId, lineId, nature, startDate, endDate);
    }

    @GetMapping("map")
    @ApiOperation("获取地图展示数据")
    @Log("获取地图展示数据")
    public JiebaoResponse map(String startDate, String endDate, Integer status) {
        return caseService.map(startDate, endDate, status);
    }

    @PostMapping("lock")
    @ApiOperation("是否锁定")
    public JiebaoResponse lock(String[] caseId, String month, Integer status) {
        return caseService.lock(caseId, month, status);
    }
}
