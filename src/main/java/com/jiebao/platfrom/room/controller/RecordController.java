package com.jiebao.platfrom.room.controller;


import com.jiebao.platfrom.common.annotation.Log;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.domain.QueryRequest;
import com.jiebao.platfrom.room.domain.Record;
import com.jiebao.platfrom.room.domain.Room;
import com.jiebao.platfrom.room.service.IMessageService;
import com.jiebao.platfrom.room.service.IRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
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
@RequestMapping("/room/record")
@Api(tags = "room-会议申请")
public class RecordController {
    @Autowired
    IRecordService recordService;
    @Autowired
    IMessageService messageService;

    @PostMapping(value = "saveOrUpdate")
    @ApiOperation("会议的创建以及修改")
    @Log(value = "会议的创建以及修改")
    private JiebaoResponse saveOrUpdate(Record record) {
        return new JiebaoResponse().message(recordService.saveOrUpdate(record) ? "操作成功" : "操作失败");
    }

    @DeleteMapping(value = "delete/{id}")
    @ApiOperation("会议删除")
    @Log(value = "会议删除")
    private JiebaoResponse deleteById(String id) {
        return new JiebaoResponse().message(recordService.removeById(id) ? "操作成功" : "操作失败");
    }

//    @GetMapping(value = "list")
//    @ApiOperation("会议查询")
//    @Log(value = "会议查询")
//    private JiebaoResponse list(QueryRequest queryRequest, String name, String address, Integer small, Integer big) {
//        return new JiebaoResponse().data(recordService.list(queryRequest, name, address, small, big)).message("查询成功");
//    }

    @GetMapping(value = "getRecord")
    @ApiOperation("查询指定会议")
    @Log(value = "查询指定会议")
    private JiebaoResponse getRecord(String id) {
        return new JiebaoResponse().data(recordService.getById(id)).message("查询成功");
    }

    @GetMapping("sendEmail")
    @ApiOperation("发送会议信息")
    @Log(value = "发送会议信息")
    private JiebaoResponse sendEmail(String recordId, String message, Integer inviteIf, List<String> listUserID) {
        return recordService.sendEmail(recordId, message, inviteIf, listUserID);
    }

    @GetMapping("getRecordByRoomIdOrDateOrUserId")
    @ApiOperation("通过会议室查询所属绑定会议")
    @Log("通过会议室查询所属绑定会议")
    private JiebaoResponse getRecordByRoomIdOrDateOrUserId(QueryRequest queryRequest, Room roomId, Date date, String userId, String order) {
        return recordService.getRecordByRoomIdOrDateOrUserId(queryRequest, roomId, date, userId, order);
    }


}
