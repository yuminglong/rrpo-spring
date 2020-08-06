package com.jiebao.platfrom.room.controller;


import com.jiebao.platfrom.common.annotation.Log;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.domain.QueryRequest;
import com.jiebao.platfrom.room.service.IRecordService;
import com.jiebao.platfrom.room.service.IRecordSeviceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author qta
 * @since 2020-07-20
 */
@RestController
@RequestMapping("/room/record-sevice")
@Api(tags = "room-服务项绑定")
public class RecordSeviceController {
 @Autowired
    IRecordSeviceService recordSeviceService;

    @ApiOperation("创建服务项绑定")
    @Log(value = "创建服务项绑定")
    @PostMapping(value = "addLead")
    public JiebaoResponse addLead(String id, List<String> leadListId) {
        return new JiebaoResponse().message(recordSeviceService.addLead(id, leadListId) ? "添加成功" : "上传成功");
    }


    @Delete(value = "delete/{idList}")
    @ApiOperation("批量 服务项绑定删除")
    @Log(value = "服务项绑定删除")
    private JiebaoResponse deleteById(@PathVariable String[] idList) {
        return new JiebaoResponse().message(recordSeviceService.deleteByListId(Arrays.asList(idList) ) ? "操作成功" : "操作失败");
    }

    @GetMapping(value = "select")
    @ApiOperation("根据会议查询 服务项绑定")
    @Log(value = "根据会议查询 服务项绑定")
    private JiebaoResponse getFile(String id, QueryRequest queryRequest) {
        return new JiebaoResponse().data(recordSeviceService.list(queryRequest,id)).message("查询成功");
    }
}
