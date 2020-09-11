package com.jiebao.platfrom.accident.controller;


import com.jiebao.platfrom.accident.daomain.Accident;
import com.jiebao.platfrom.accident.service.IAccidentService;
import com.jiebao.platfrom.common.annotation.Log;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.domain.QueryRequest;
import io.swagger.annotations.Api;
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
 * @since 2020-08-04
 */
@RestController
@RequestMapping("/accident/accident")
@Api(tags = "accident-事故信息总表")
public class AccidentController {
    @Autowired
    IAccidentService accidentService;

    @PostMapping("saveOrUpdate")
    @ApiOperation("添加修改 事故信息")
    @Log("添加修改 事故信息")
    public JiebaoResponse saveOrUpdate(Accident accident) {
        JiebaoResponse jiebaoResponse = new JiebaoResponse();
        if (accident.getAccidentId() != null) {
            Accident accident1 = accidentService.getById(accident.getAccidentId()); //数据库已存在德
            if (accident1.getStatu() == 1)
                return jiebaoResponse.failMessage("已锁定不可操作");
        } else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
            try {
                accident.setMonth(simpleDateFormat.format(simpleDateFormat.parse(accident.getDate())));
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        jiebaoResponse = accidentService.saveOrUpdate(accident) ? jiebaoResponse.okMessage("操作成功") : jiebaoResponse.failMessage("操作失败");
        return jiebaoResponse;
    }

    @DeleteMapping("deleteByLists/{lists}")
    @ApiOperation("集合删除事故信息")
    @Log("集合删除事故信息")
    public JiebaoResponse deleteByLists(@PathVariable String[] lists) {
        JiebaoResponse jiebaoResponse = new JiebaoResponse();
        jiebaoResponse = accidentService.removeByIds(Arrays.asList(lists)) ? jiebaoResponse.okMessage("删除成功") : jiebaoResponse.failMessage("删除失败");
        return jiebaoResponse;
    }

    @GetMapping("listPage")
    @ApiOperation("条件查询分页  事故信息")
    @Log("条件查询分页  事故信息")
    public JiebaoResponse list(QueryRequest queryRequest, String cityCsId, String cityQxId, String startDate, String endDate) {
        return accidentService.list(queryRequest, cityCsId, cityQxId, startDate, endDate);
    }

    @GetMapping("map")
    @ApiOperation("获取地图展示数据")
    @Log("获取地图展示数据")
    public JiebaoResponse map(String startDate, String endDate, Integer status) {
        return accidentService.map(startDate, endDate, status);
    }


    @PostMapping("lock")
    @ApiOperation("是否锁定")
    public JiebaoResponse lock(String[] accidentId, String month, Integer status) {
        return accidentService.lock(accidentId, month, status);
    }


}
