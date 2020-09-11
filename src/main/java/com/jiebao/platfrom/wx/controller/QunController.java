package com.jiebao.platfrom.wx.controller;


import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.domain.QueryRequest;
import com.jiebao.platfrom.wx.domain.Qun;
import com.jiebao.platfrom.wx.service.IQunService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author qta
 * @since 2020-08-20
 */
@RestController
@RequestMapping("/wx/qun")
@Api(tags = "wx_微信群建立")
public class QunController {
    @Autowired
    IQunService qunService;

    @PostMapping("addOrUpdate")
    @ApiOperation("群添加修改")
    public JiebaoResponse saveOrUpdate(Qun qun) {
        return qunService.addOrUpdate(qun);
    }


    @GetMapping("list")
    @ApiOperation("群查询")
    public JiebaoResponse pageList(QueryRequest queryRequest, String name, String userName) {
        return qunService.pageList(queryRequest, name, userName);
    }
//
//    @GetMapping("up")
//    @ApiOperation("上报审核")
//    public JiebaoResponse up(String qunId) {
//        return qunService.up(qunId);
//    }

}
