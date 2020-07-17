package com.jiebao.platfrom.railway.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiebao.platfrom.common.annotation.Log;
import com.jiebao.platfrom.common.controller.BaseController;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.domain.QueryRequest;
import com.jiebao.platfrom.common.exception.JiebaoException;
import com.jiebao.platfrom.railway.dao.AreaMapper;
import com.jiebao.platfrom.railway.domain.Address;
import com.jiebao.platfrom.railway.domain.Area;
import com.jiebao.platfrom.railway.domain.Inform;
import com.jiebao.platfrom.railway.service.AddressService;
import com.jiebao.platfrom.railway.service.AreaService;
import com.wuwenze.poi.ExcelKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.*;


/**
 * @author yf
 */
@Slf4j
@RestController
@RequestMapping(value = "/area")
@Api(tags = "地区")   //swagger2 api文档说明示例
public class AreaController extends BaseController {

    private String message;


    @Autowired
    private AreaService areaService;

    @Autowired
    private AddressService addressService;

    /**
     * 使用Mapper操作数据库
     *
     * @return JiebaoResponse 标准返回数据类型
     */
    @GetMapping
    @ApiOperation(value = "查询数据List", notes = "查询数据List列表", response = JiebaoResponse.class, httpMethod = "GET")
    public Map<String, Object> getAreaListByService(QueryRequest request, Area area) {
        return this.areaService.getAreaListByService(request, area);
    }

    /**
     * 分页查询
     *
     * @param request - 分页参数
     * @return
     * @Parameters sortField  according to order by Field
     * @Parameters sortOrder  JiebaoConstant.ORDER_ASC or JiebaoConstant.ORDER_DESC
     */
    @PostMapping("/getAreaList")
    @ApiOperation(value = "分页查询", notes = "查询分页数据", response = JiebaoResponse.class, httpMethod = "POST")
    public JiebaoResponse getAreaList(QueryRequest request) {
        IPage<Area> areaList = areaService.getAreaList(request);
        return new JiebaoResponse().data(this.getDataTable(areaList));
    }

    @DeleteMapping("/{ids}")
    @Log("删除")
    @ApiOperation(value = "批量删除", notes = "批量删除", response = JiebaoResponse.class, httpMethod = "DELETE")
    @Transactional(rollbackFor = Exception.class)
    public JiebaoResponse delete(@PathVariable String[] ids) throws JiebaoException {
        try {
            Arrays.stream(ids).forEach(id -> {
                areaService.removeById(id);
            });
        } catch (Exception e) {
            throw new JiebaoException("删除失败");
        }
        return new JiebaoResponse().message("删除成功");
    }

    @PostMapping
    @Log("新增")
    @ApiOperation(value = "新增地区", notes = "新增地区", httpMethod = "POST")
    @Transactional(rollbackFor = Exception.class)
    public JiebaoResponse addArea(@Valid Area area) {
        areaService.save(area);
        return new JiebaoResponse().message("成功");
    }


    @PostMapping(value = "/excel")
    @ApiOperation(value = "导出", notes = "导出", httpMethod = "POST")
    //@RequiresPermissions("inform:export")
    public void export(Area area, QueryRequest request, HttpServletResponse response) throws JiebaoException {
        try {
            List<Area> areas = this.areaService.getAreaList(request, area);
            ExcelKit.$Export(Area.class, response).downXlsx(areas, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new JiebaoException(message);
        }
    }

    @PutMapping
    @Log("修改地区")
    @ApiOperation(value = "修改地区", notes = "修改地区", httpMethod = "PUT")
    @Transactional(rollbackFor = Exception.class)
    public void updateArea(@Valid Area area) throws JiebaoException {
        try {
            this.areaService.updateByKey(area);
        } catch (Exception e) {
            message = "修改地区失败";
            log.error(message, e);
            throw new JiebaoException(message);
        }
    }


}
