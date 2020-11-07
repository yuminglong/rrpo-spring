package com.jiebao.platfrom.accident.controller;


import com.jiebao.platfrom.accident.daomain.Jk;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.domain.QueryRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author qta
 * @since 2020-11-07
 */
@RestController
@RequestMapping("/accident/jk")
@Api(tags = "accident-监控")
public class JkController {


    @GetMapping("list")
    @ApiOperation("查询")
    public JiebaoResponse listPage(QueryRequest queryRequest) {
        return null;
    }
    @GetMapping("excel")
    @ApiOperation("导出")
    public JiebaoResponse importExcel() {
        return null;
    }

    @PostMapping("saveOrUpdate")
    @ApiOperation("新增或者修改")
    public JiebaoResponse saveOrUpdate(Jk jk) {
        return null;
    }

    @DeleteMapping("delete")
    @ApiOperation("删除")
    public JiebaoResponse delete(String[] idList){
        return null;
    }
}
