package com.jiebao.platfrom.accident.controller;


import com.jiebao.platfrom.accident.daomain.Jk;
import com.jiebao.platfrom.accident.daomain.Ql;
import com.jiebao.platfrom.accident.service.IJkService;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.domain.QueryRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

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

    @Autowired
    IJkService jkService;

    @GetMapping("list")
    @ApiOperation("查询")
    public JiebaoResponse listPage(QueryRequest queryRequest,String gac,String dzs) {
        try {
            return new JiebaoResponse().data(jkService.listPage(queryRequest, gac, dzs)).okMessage("查询成功");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new JiebaoResponse().failMessage("查询失败");
        }
    }

    @GetMapping("excel")
    @ApiOperation("导出")
    public JiebaoResponse importExcel(HttpServletResponse response,String gac,String dzs) {
        return new JiebaoResponse().data(jkService.importExcel(response, gac, dzs)) ;
    }

    @PostMapping("saveOrUpdate")
    @ApiOperation("新增或者修改")
    public JiebaoResponse saveOrUpdate(Jk jk) {
        try {
            jkService.addOrUpdate(jk);
            return new JiebaoResponse().data(jk).okMessage("操作成功");
        } catch (Exception e) {
            return new JiebaoResponse().failMessage("操作失败");
        }
    }

    @DeleteMapping("delete")
    @ApiOperation("删除")
    public JiebaoResponse delete(String[] idList) {
        try {
            jkService.delete(idList);
            return new JiebaoResponse().okMessage("删除");
        } catch (Exception e) {
            return new JiebaoResponse().failMessage("操作失败");
        }
    }
}
