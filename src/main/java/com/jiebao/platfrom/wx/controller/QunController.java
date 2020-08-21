package com.jiebao.platfrom.wx.controller;


import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.domain.QueryRequest;
import com.jiebao.platfrom.wx.domain.Qun;
import com.jiebao.platfrom.wx.service.IQunService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
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
@Api(tags = "微信群建立")
public class QunController {
    @Autowired
    IQunService qunService;

    @PostMapping("addOrUpdate")
    @ApiOperation("群添加修改")
    public JiebaoResponse saveOrUpdate(Qun qun) {
        JiebaoResponse jiebaoResponse = new JiebaoResponse();
        jiebaoResponse = qunService.saveOrUpdate(qun) ? jiebaoResponse.okMessage("操作成功") : jiebaoResponse.failMessage("操作失败");
        return jiebaoResponse;
    }


    @PostMapping("list")
    @ApiOperation("群查询")
    public JiebaoResponse saveOrUpdate(QueryRequest queryRequest,String name,String userName) {
        JiebaoResponse jiebaoResponse = new JiebaoResponse();
      //  jiebaoResponse = qunService.saveOrUpdate(qun) ? jiebaoResponse.okMessage("操作成功") : jiebaoResponse.failMessage("操作失败");
        return jiebaoResponse;
    }

}
