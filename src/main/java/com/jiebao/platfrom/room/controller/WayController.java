package com.jiebao.platfrom.room.controller;


import com.jiebao.platfrom.common.annotation.Log;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.room.service.IWayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author qta
 * @since 2020-07-20
 */
@RestController
@RequestMapping("/room/way")
@Api(tags = "room-消息发送方式")
public class WayController {
    @Autowired
    IWayService wayService;

    @ApiOperation("绑定消息发送方式")
    @Log(value = "绑定消息发送方式")
    @PostMapping(value = "addLead")
    public JiebaoResponse addLead(String id, List<Integer> leadListId) {

        return new JiebaoResponse().message(wayService.addLead(id, leadListId) ? "添加成功" : "上传成功");
    }


    @GetMapping(value = "delete/{idList}")
    @ApiOperation("批量 绑定消息发送方式")
    @Log(value = "批量 绑定消息发送方式")
    private JiebaoResponse deleteById(@PathVariable String[] idList) {
        return new JiebaoResponse().message(wayService.deleteByListId(Arrays.asList(idList)) ? "操作成功" : "操作失败");
    }

    @GetMapping(value = "select")
    @ApiOperation("根据会议查询 绑定消息发送方式")
    @Log(value = "根据会议查询 绑定消息发送方式")
    private JiebaoResponse getFile(String id) {
        return new JiebaoResponse().data(wayService.list(id)).message("查询成功");
    }
}
