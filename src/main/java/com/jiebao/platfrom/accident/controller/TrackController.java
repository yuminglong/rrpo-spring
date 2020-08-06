package com.jiebao.platfrom.accident.controller;


import com.jiebao.platfrom.accident.daomain.Track;
import com.jiebao.platfrom.accident.daomain.Train;
import com.jiebao.platfrom.accident.service.ITrackService;
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
@RequestMapping("/accident/track")
@Api(tags = "accident-公务段")
public class TrackController {
    @Autowired
    ITrackService trackService;

    @PostMapping("saveOrUpdate")
    @ApiOperation("添加修改  公务段")
    public JiebaoResponse saveOrUpdate(Track track) {
        return new JiebaoResponse().message(trackService.saveOrUpdate(track) ? "操作成功" : "操作失败");
    }

    @GetMapping("list")
    @ApiOperation("集合")
    public JiebaoResponse list() {
        return new JiebaoResponse().data(trackService.list()).message("查询成功");
    }

    @DeleteMapping("deleteByLists/{lists}")
    @ApiOperation("集合删除")
    public JiebaoResponse deleteByLists(List<String> lists) {
        return new JiebaoResponse().message(trackService.removeByIds(lists) ? "删除成功" : "删除失败");
    }
}
