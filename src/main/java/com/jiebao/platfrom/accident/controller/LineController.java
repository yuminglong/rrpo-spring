package com.jiebao.platfrom.accident.controller;


import com.jiebao.platfrom.accident.daomain.Line;
import com.jiebao.platfrom.accident.daomain.Track;
import com.jiebao.platfrom.accident.service.ILineService;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author qta
 * @since 2020-08-04
 */
@RestController
@RequestMapping("/accident/line")
@Api(tags = "线路段")
public class LineController {

   @Autowired
    ILineService lineService;

    @PostMapping("saveOrUpdate")
    @ApiOperation("添加修改  线路段")
    public JiebaoResponse saveOrUpdate(Line line) {
        return new JiebaoResponse().message(lineService.saveOrUpdate(line) ? "操作成功" : "操作失败");
    }

    @GetMapping("list")
    @ApiOperation("集合")
    public JiebaoResponse list() {
        return new JiebaoResponse().data(lineService.list()).message("查询成功");
    }

    @DeleteMapping("deleteByLists")
    @ApiOperation("集合删除")
    public JiebaoResponse deleteByLists(List<String> lists) {
        return new JiebaoResponse().message(lineService.removeByIds(lists) ? "删除成功" : "删除失败");
    }
}
