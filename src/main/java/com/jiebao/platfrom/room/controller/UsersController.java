package com.jiebao.platfrom.room.controller;


import com.jiebao.platfrom.common.annotation.Log;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.domain.QueryRequest;
import com.jiebao.platfrom.room.service.IUsersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Delete;
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
@RequestMapping("/room/user")
@Api(tags = "room-参会人员")
public class UsersController {
    @Autowired
    IUsersService usersService;

    @ApiOperation("创建会议参会人员")
    @Log(value = "创建会议参会人员")
    @PostMapping(value = "addLead")
    public JiebaoResponse addLead(String id, List<String> leadListId) {
        return usersService.addUSer(id, leadListId);
    }


    @Delete(value = "delete/{idList}")
    @ApiOperation("批量 参会人员")
    @Log(value = "领导删除")
    private JiebaoResponse deleteById( List<String> idList) {
        return new JiebaoResponse().message(usersService.deleteByListId(idList) ? "操作成功" : "操作失败");
    }

    @GetMapping(value = "select")
    @ApiOperation("根据会议查询 参会人员")
    @Log(value = "根据会议查询 参会人员")
    private JiebaoResponse getFile(String id, QueryRequest queryRequest,Integer leadIf) {
        return new JiebaoResponse().data(usersService.list(queryRequest,id,leadIf)).message("查询成功");
    }
}
