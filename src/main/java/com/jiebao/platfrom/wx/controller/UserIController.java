package com.jiebao.platfrom.wx.controller;


import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.domain.QueryRequest;
import com.jiebao.platfrom.wx.domain.Qun;
import com.jiebao.platfrom.wx.domain.UserI;
import com.jiebao.platfrom.wx.service.IUserIService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Delete;
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
@RequestMapping("/wx/user")
@Api(tags = "微信群用户信息")
public class UserIController {
    @Autowired
    IUserIService userIService;

    @PostMapping("addOrUpdate")
    @ApiOperation("群添加修改")
    public JiebaoResponse saveOrUpdate(UserI userI) {
        JiebaoResponse jiebaoResponse = new JiebaoResponse();
        jiebaoResponse = userIService.saveOrUpdate(userI) ? jiebaoResponse.okMessage("操作成功") : jiebaoResponse.failMessage("操作失败");
        return jiebaoResponse;
    }

    @Delete("deleteS")
    @ApiOperation("删除集合")
    public JiebaoResponse deleteS(String[] wxUserIdS) {
        return userIService.deleteS(wxUserIdS);
    }

    @GetMapping("List")
    @ApiOperation("查询群成员")
    public JiebaoResponse list(QueryRequest queryRequest, String name, String qunId) {
        return userIService.list(queryRequest, name, qunId);
    }
}
