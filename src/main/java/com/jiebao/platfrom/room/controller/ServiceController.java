package com.jiebao.platfrom.room.controller;


import com.jiebao.platfrom.common.annotation.Log;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.domain.QueryRequest;
import com.jiebao.platfrom.room.domain.Service;
import com.jiebao.platfrom.room.service.IServiceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author qta
 * @since 2020-07-20
 */
@RestController
@RequestMapping("/room/service")
@Api(tags = "会议服务")
public class ServiceController {
    @Autowired
    IServiceService serviceService;

    @PostMapping(value = "saveOrUpdate")
    @ApiOperation("服务的创建以及修改")
    @Log(value = "服务的创建以及修改")
    private JiebaoResponse saveOrUpdate(Service service) {
        return new JiebaoResponse().message(serviceService.saveOrUpdate(service) ? "操作成功" : "操作失败");
    }

    @DeleteMapping(value = "delete/{id}")
    @ApiOperation("服务删除")
    @Log(value = "服务删除")
    private JiebaoResponse deleteById(String id) {
        return new JiebaoResponse().message(serviceService.removeById(id) ? "操作成功" : "操作失败");
    }

    @GetMapping(value = "list")
    @ApiOperation("服务查询")
    @Log(value = "服务查询")
    private JiebaoResponse list(QueryRequest queryRequest, String name, String address, Integer small, Integer big) {
        return new JiebaoResponse().data(serviceService.list()).message("查询成功");
    }

    @GetMapping(value = "getService")
    @ApiOperation("查询指定服务")
    @Log(value = "查询指定服务")
    private JiebaoResponse getService(String id) {
        return new JiebaoResponse().data(serviceService.getById(id)).message("查询成功");
    }
}
