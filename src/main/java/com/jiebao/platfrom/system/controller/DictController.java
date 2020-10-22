package com.jiebao.platfrom.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.jiebao.platfrom.common.annotation.Log;
import com.jiebao.platfrom.common.controller.BaseController;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.domain.QueryRequest;
import com.jiebao.platfrom.common.exception.JiebaoException;
import com.jiebao.platfrom.system.domain.Dict;
import com.jiebao.platfrom.system.service.DictService;
import com.wuwenze.poi.ExcelKit;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Validated
@RestController
@RequestMapping("dict")
public class DictController extends BaseController {

    private String message;
    private static final String zero ="0";

    @Autowired
    private DictService dictService;

  /*  @GetMapping
//    @RequiresPermissions("dict:view")
    public Map<String, Object> DictList(QueryRequest request, Dict dict) {
        return getDataTable(this.dictService.findDicts(request, dict));
    }

    @Log("新增字典")
    @PostMapping
    @RequiresPermissions("dict:add")
    public void addDict(@Valid Dict dict) throws JiebaoException {
        try {
            this.dictService.createDict(dict);
        } catch (Exception e) {
            message = "新增字典成功";
            log.error(message, e);
            throw new JiebaoException(message);
        }
    }

    @Log("删除字典")
    @DeleteMapping("/{dictIds}")
    @RequiresPermissions("dict:delete")
    public void deleteDicts(@NotBlank(message = "{required}") @PathVariable String dictIds) throws JiebaoException {
        try {
            String[] ids = dictIds.split(StringPool.COMMA);
            this.dictService.deleteDicts(ids);
        } catch (Exception e) {
            message = "删除字典成功";
            log.error(message, e);
            throw new JiebaoException(message);
        }
    }

    @Log("修改字典")
    @PutMapping
    @RequiresPermissions("dict:update")
    public void updateDict(@Valid Dict dict) throws JiebaoException {
        try {
            this.dictService.updateDict(dict);
        } catch (Exception e) {
            message = "修改字典成功";
            log.error(message, e);
            throw new JiebaoException(message);
        }
    }

    @PostMapping("excel")
    @RequiresPermissions("dict:export")
    public void export(QueryRequest request, Dict dict, HttpServletResponse response) throws JiebaoException {
        try {
            List<Dict> dicts = this.dictService.findDicts(request, dict).getRecords();
            ExcelKit.$Export(Dict.class, response).downXlsx(dicts, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new JiebaoException(message);
        }
    }*/

    @PostMapping("addTable")
   // @RequiresPermissions("dict:addTable")
    @ApiOperation(value = "新增", notes = "新增", response = JiebaoResponse.class, httpMethod = "POST")
    public JiebaoResponse addTable( Dict dict)  {
        return this.dictService.createNewDict(dict);
    }

    @Log("删除字典")
    @PostMapping("deleteTable")
   // @RequiresPermissions("dict:deleteTable")
    @ApiOperation(value = "删除", notes = "删除", response = JiebaoResponse.class, httpMethod = "POST")
    public void deleteTable( String [] dictIds) throws JiebaoException {
        try {
            //String[] ids = dictIds.split(StringPool.COMMA);
           // dictService.removeByIds(dictIds);
            Arrays.stream(dictIds).forEach(id -> {
                Dict byId = dictService.getById(id);
                if (zero.equals(byId.getParentId())){
                    Map<String, Object> map = new HashMap<>();
                    map.put("parent_id",id);
                    dictService.removeByMap(map);
                }
                dictService.removeById(id);
            });
        } catch (Exception e) {
            message = "删除失败";
            log.error(message, e);
            throw new JiebaoException(message);
        }
    }

    @PostMapping("updateTable")
  //  @RequiresPermissions("dict:updateTable")
    @ApiOperation(value = "修改", notes = "修改", response = JiebaoResponse.class, httpMethod = "POST")
    public JiebaoResponse updateTable( Dict dict)  {
        boolean newDict = this.dictService.updateNewDict(dict);
        if (newDict){
            return new JiebaoResponse().okMessage("成功");
        }
        else {
            return new JiebaoResponse().failMessage("失败");
        }
    }

    @GetMapping("getListTable")
  //  @RequiresPermissions("dict:getListTable")
    @ApiOperation(value = "查询", notes = "查询", response = JiebaoResponse.class, httpMethod = "GET")
    public JiebaoResponse getListTable(QueryRequest queryRequest, Dict dict){
        IPage<Dict> listTable = this.dictService.getListTable(queryRequest, dict);
        return new JiebaoResponse().data(listTable).okMessage("查询成功");
    }
}
