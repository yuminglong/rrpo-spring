package com.jiebao.platfrom.room.controller;


import com.jiebao.platfrom.common.annotation.Log;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.domain.QueryRequest;
import com.jiebao.platfrom.common.utils.uploadUtil;
import com.jiebao.platfrom.room.service.ILeadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
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
@RequestMapping("/room/lead")
@Api(tags = "参会领导人")
public class LeadController {
    @Autowired
    ILeadService leadService;

    @ApiOperation("创建会议领导人")
    @Log(value = "创建会议领导人")
    @PostMapping(value = "addLead")
    public JiebaoResponse addLead(String id, List<String> leadListId) {
        return new JiebaoResponse().message(leadService.addLead(id, leadListId) ? "添加成功" : "上传成功");
    }


    @GetMapping(value = "delete")
    @ApiOperation("批量 领导删除")
    @Log(value = "领导删除")
    private JiebaoResponse deleteById(String id, List<String> idList) {
        return new JiebaoResponse().message(leadService.deleteByListId(id, idList) ? "操作成功" : "操作失败");
    }

    @GetMapping(value = "select")
    @ApiOperation("根据会议查询 领导")
    @Log(value = "根据会议查询 领导")
    private JiebaoResponse getFile(String id, QueryRequest queryRequest) {
        return new JiebaoResponse().data(leadService.list(queryRequest,id)).message("查询成功");
    }
}
