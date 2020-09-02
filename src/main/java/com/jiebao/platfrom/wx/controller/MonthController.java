package com.jiebao.platfrom.wx.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.domain.QueryRequest;
import com.jiebao.platfrom.system.domain.File;
import com.jiebao.platfrom.system.service.FileService;
import com.jiebao.platfrom.wx.domain.Month;
import com.jiebao.platfrom.wx.service.IMonthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author qta
 * @since 2020-08-22
 */
@RestController
@RequestMapping("/wx/month")
@Api(tags = "wx_月度评选")
public class MonthController {
    @Autowired
    IMonthService monthService;
    @Autowired
    FileService fileService;

    @PostMapping("saveorUpdate")
    @ApiOperation("添加或者修改")
    public JiebaoResponse saveOrUpdate(Month month, String[] fileIds) {
        JiebaoResponse jiebaoResponse = new JiebaoResponse();
        boolean b = monthService.saveOrUpdate(month);
        if (fileIds!= null) {
            if (b) {
                UpdateWrapper<File> updateWrapper = new UpdateWrapper<>();
                updateWrapper.in("file_id",Arrays.asList(fileIds));
                updateWrapper.set("ref_id",month.getWxMonthId());
                fileService.update(updateWrapper);
            }
        }
        jiebaoResponse = b ? jiebaoResponse.okMessage("操作成功") : jiebaoResponse.failMessage("操作失败");
        return jiebaoResponse;
    }

    @Delete("delete")
    @ApiOperation("删除  ")
    public JiebaoResponse saveOrUpdate(String[] ids) {
        JiebaoResponse jiebaoResponse = new JiebaoResponse();
        jiebaoResponse = monthService.removeByIds(Arrays.asList(ids)) ? jiebaoResponse.okMessage("删除成功") : jiebaoResponse.failMessage("删除失败");
        return jiebaoResponse;
    }

    @GetMapping("list")
    @ApiOperation("查询集合")
    public JiebaoResponse pageList(QueryRequest queryRequest, String month, Integer look) {
        return monthService.pageList(queryRequest, month, look);
    }

    @GetMapping("appear")
    @ApiOperation("上报")
    public JiebaoResponse appear(String monthId) {
        return monthService.appear(monthId);
    }

    @GetMapping("getById")
    @ApiOperation("查看具体信息")
    public JiebaoResponse getById(String monthId) {
        return new JiebaoResponse().data(monthService.getById(monthId)).okMessage("查询成功");
    }
}
