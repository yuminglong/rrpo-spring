package com.jiebao.platfrom.accident.controller;


import com.jiebao.platfrom.accident.daomain.Accident;
import com.jiebao.platfrom.accident.daomain.Case;
import com.jiebao.platfrom.accident.service.ICaseService;
import com.jiebao.platfrom.common.annotation.Log;
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
@RequestMapping("/accident/case")
@Api(tags = "accident-设铁案")
public class CaseController {
   @Autowired
    ICaseService caseService;

    @PostMapping("saveOrUpdate")
    @ApiOperation("添加修改 事故信息")
    @Log("添加修改 事故信息")
    public JiebaoResponse saveOrUpdate(Case ca) {
        return new JiebaoResponse().message(caseService.saveOrUpdate(ca) ? "操作成功" : "操作失败");
    }

    @DeleteMapping("deleteByLists")
    @ApiOperation("集合删除事故信息")
    @Log("集合删除事故信息")
    public JiebaoResponse deleteByLists(@RequestParam("lists") List<String> lists) {
        return new JiebaoResponse().message(caseService.removeByIds(lists) ? "删除成功" : "删除失败");
    }

    @GetMapping("listPage")
    @ApiOperation("条件查询分页  事故信息")
    @Log("条件查询分页  事故信息")
    public JiebaoResponse list(QueryRequest queryRequest, String cityCsId, String cityQxId, String startDate, String endDate) {
        return caseService.list(queryRequest, cityCsId, cityQxId, startDate, endDate);
    }
}
