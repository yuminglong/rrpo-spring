package com.jiebao.platfrom.room.controller;


import com.jiebao.platfrom.common.annotation.Log;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.room.service.IRecordFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/room/record-file")
@Api(tags = "附件绑定")
public class RecordFileController {
    @Autowired
    IRecordFileService recordFileService;

    @ApiOperation("创建附件绑定")
    @Log(value = "创建附件绑定")
    @PostMapping(value = "addLead")
    public JiebaoResponse addLead(String id, List<String> leadListId,Integer state) {

        return new JiebaoResponse().message(recordFileService.addLead(id, leadListId) ? "添加成功" : "上传成功");
    }


    @GetMapping(value = "delete")
    @ApiOperation("批量 附件绑定删除")
    @Log(value = "附件绑定删除")
    private JiebaoResponse deleteById(List<String> idList) {
        return new JiebaoResponse().message(recordFileService.deleteByListId( idList) ? "操作成功" : "操作失败");
    }

    @GetMapping(value = "select")
    @ApiOperation("根据会议查询 附件绑定")
    @Log(value = "根据会议查询 附件绑定")
    private JiebaoResponse getFile(String id) {
        return new JiebaoResponse().data(recordFileService.list(id)).message("查询成功");
    }


}
