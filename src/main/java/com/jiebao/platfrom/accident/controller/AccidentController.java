package com.jiebao.platfrom.accident.controller;


import com.jiebao.platfrom.accident.daomain.Accident;
import com.jiebao.platfrom.accident.daomain.CompareTable;
import com.jiebao.platfrom.accident.service.IAccidentService;
import com.jiebao.platfrom.accident.service.IDeptService;
import com.jiebao.platfrom.common.annotation.Log;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.domain.QueryRequest;
import com.jiebao.platfrom.common.utils.ExportExcel;
import com.jiebao.platfrom.system.service.DeptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;

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
    @Autowired
    IDeptService deptService;
    @Autowired
    DeptService deptServices;


    @PostMapping("saveOrUpdate")
    @ApiOperation("添加修改 事故信息")
    @Log("添加修改 事故信息")
    public JiebaoResponse saveOrUpdate(Accident accident) {
        JiebaoResponse jiebaoResponse = new JiebaoResponse();

        if (accident.getPoliceId() != null) {
            if (deptServices.getById(accident.getPoliceId()).getRank() == 4) {
                return jiebaoResponse.failMessage("请选中对应的派出所");
            }
            accident.setPoliceFather(deptServices.getById(accident.getPoliceId()).getParentId());
        }
        if (accident.getAccidentId() != null) {
            Accident accident1 = accidentService.getById(accident.getAccidentId()); //数据库已存在德
            if (accident1.getStatu() != null && accident1.getStatu() == 1)
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
    public JiebaoResponse list(QueryRequest queryRequest, String policeId, String cityLevelId, String quDeptId, String startDate, String endDate) {
        return accidentService.list(queryRequest, policeId, cityLevelId, quDeptId, startDate, endDate);
    }

    @GetMapping("map")
    @ApiOperation("获取地图展示数据")
    @Log("获取地图展示数据")
    public JiebaoResponse map(String policeId, String cityLevelId, String startDate, String endDate, String quDeptId) {
        return accidentService.map(policeId, cityLevelId, startDate, endDate, quDeptId);
    }


    @PostMapping("lock")
    @ApiOperation("是否锁定")
    public JiebaoResponse lock(String[] accidentId, String month, Integer status) {
        return accidentService.lock(accidentId, month, status);
    }

    @PostMapping("func")
    @ApiOperation("系数求出")
    public JiebaoResponse func(String nature, String instationSection, String road, String age, String closed, String jzd, String distance, String identity, String conditions) {
        return new JiebaoResponse().data(accidentService.func(nature, instationSection, road, age, closed, jzd, distance, identity, conditions)).okMessage("操作成功");
    }

    @GetMapping("compareTable")
    @ApiOperation("同期比较")
    public JiebaoResponse compareTable(String startYear, String endYear) {
        return new JiebaoResponse().okMessage("操作成功").data(accidentService.compareTable(startYear, endYear));
    }

    @GetMapping("compareTableExcel")
    @ApiOperation("同期比较导出")
    public void compareTableImpot(String startYear, String endYear, HttpServletResponse response) {
        ExportExcel.exportExcelList(accidentService.compareTable(startYear, endYear), CompareTable.class, response);
    }

}
