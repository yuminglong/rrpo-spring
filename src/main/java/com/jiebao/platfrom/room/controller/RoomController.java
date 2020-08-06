package com.jiebao.platfrom.room.controller;


import com.jiebao.platfrom.common.annotation.Log;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.domain.QueryRequest;
import com.jiebao.platfrom.room.domain.Room;
import com.jiebao.platfrom.room.service.IRoomService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author qta
 * @since 2020-07-20
 */
@RestController
@RequestMapping("/room/room")
@Api(tags = "room-会议室")
public class RoomController {
    @Autowired
    IRoomService roomService;

    @PostMapping(value = "saveOrUpdate")
    @ApiOperation("会议室的创建以及修改")
    @Log(value = "会议室的创建以及修改")
    private JiebaoResponse saveOrUpdate(Room room) {
        return new JiebaoResponse().message(roomService.saveOrUpdate(room) ? "操作成功" : "操作失败");
    }

    @DeleteMapping(value = "delete/{ids}")
    @ApiOperation("会议室删除")
    @Log(value = "会议室删除")
    private JiebaoResponse deleteById(@PathVariable String[] ids) {
        return new JiebaoResponse().message(roomService.removeByIds(Arrays.asList(ids)) ? "操作成功" : "操作失败");
    }

    @GetMapping(value = "list")
    @ApiOperation("会议室查询")
    @Log(value = "会议室查询")
    private JiebaoResponse list(QueryRequest queryRequest, String name, String address, Integer small, Integer big) {
        return new JiebaoResponse().data(roomService.list(queryRequest, name, address, small, big)).message("查询成功");
    }

    @GetMapping(value = "getRoom")
    @ApiOperation("查询指定会议室")
    @Log(value = "查询指定会议室")
    private JiebaoResponse getRoom(String id) {
        return new JiebaoResponse().data(roomService.getById(id)).message("查询成功");
    }
}
