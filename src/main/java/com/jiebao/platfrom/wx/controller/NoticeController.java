package com.jiebao.platfrom.wx.controller;


import com.jiebao.platfrom.common.annotation.Log;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.domain.QueryRequest;
import com.jiebao.platfrom.wx.domain.Notice;
import com.jiebao.platfrom.wx.service.INoticeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author qta
 * @since 2020-09-09
 */
@RestController
@RequestMapping("/wx/notice")
@Api(tags = "wx_微信公告")
public class NoticeController {
    @Autowired
    INoticeService noticeService;

    @PostMapping("addOrUpdate")
    @ApiOperation("添加修改")
    @Log("微信护路  公告信息")
    public JiebaoResponse addOrUpdate(Notice notice) {
        return noticeService.addOrUpdate(notice);
    }


    @DeleteMapping("delete")
    @ApiOperation("删除")
    @Log("微信护路  公告信息 删除")
    public JiebaoResponse delete(String[] ids) {
        return noticeService.delete(ids);
    }


    @GetMapping("list")
    @ApiOperation("删除")
    @Log("微信护路  公告信息 删除")
    public JiebaoResponse list(String noticeId, Date StartDate, Date endStart, QueryRequest queryRequest) {
        return noticeService.list(noticeId, StartDate, endStart, queryRequest);
    }

}
