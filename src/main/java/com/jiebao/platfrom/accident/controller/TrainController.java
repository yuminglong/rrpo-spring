package com.jiebao.platfrom.accident.controller;


import com.jiebao.platfrom.accident.daomain.Train;
import com.jiebao.platfrom.accident.service.ITrainService;
import com.jiebao.platfrom.common.annotation.Log;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
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
 * @since 2020-08-04
 */
@RestController
@RequestMapping("/accident/train")
@Api(tags = "accident-车务段")
public class TrainController {
    @Autowired
    ITrainService trainService;

    @PostMapping("saveOrUpdate")
    @ApiOperation("添加修改  车务段")
    public JiebaoResponse saveOrUpdate(Train train) {
        return new JiebaoResponse().message(trainService.saveOrUpdate(train) ? "操作成功" : "操作失败");
    }

    @GetMapping("list")
    @ApiOperation("集合")
    public JiebaoResponse list() {
        return new JiebaoResponse().data(trainService.list()).message("查询成功");
    }

    @DeleteMapping("deleteByLists/{lists}")
    @ApiOperation("集合删除")
    public JiebaoResponse deleteByLists(List<String> lists) {
        return new JiebaoResponse().message(trainService.removeByIds(lists) ? "删除成功" : "删除失败");
    }
}
