package com.jiebao.platfrom.railway.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiebao.platfrom.common.annotation.Log;
import com.jiebao.platfrom.common.controller.BaseController;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.domain.QueryRequest;
import com.jiebao.platfrom.common.exception.JiebaoException;
import com.jiebao.platfrom.railway.dao.InformMapper;
import com.jiebao.platfrom.railway.domain.Inform;
import com.jiebao.platfrom.railway.service.InformService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;


/**
 * @author yf
 */
@Slf4j
@RestController
@RequestMapping(value = "/inform")
@Api(tags = "通知公告")   //swagger2 api文档说明示例
public class InformController extends BaseController {

    private String message;
    @Autowired
    private InformMapper informMapper;

    @Autowired
    private InformService informService;

    /**
     * 使用Mapper操作数据库
     *
     * @return JiebaoResponse 标准返回数据类型
     */
    @PostMapping(value = "/getInformListByMapper")
    @ApiOperation(value = "查询数据List", notes = "查询数据List列表", response = JiebaoResponse.class, httpMethod = "POST")
    public JiebaoResponse getInformListByMapper() {
        List<Inform> list = informMapper.getInformList();
        for (Inform i : list
        ) {
            i.setKey(i.getId());
        }
        return new JiebaoResponse().data(list).message("查询成功").put("remake", "其他数据返回");
    }

    /**
     * 分页查询
     *
     * @param request - 分页参数
     * @return
     * @Parameters sortField  according to order by Field
     * @Parameters sortOrder  JiebaoConstant.ORDER_ASC or JiebaoConstant.ORDER_DESC
     */
    @GetMapping
    @ApiOperation(value = "分页查询", notes = "查询分页数据", response = JiebaoResponse.class, httpMethod = "GET")
    public JiebaoResponse getInformList(QueryRequest request, Inform inform, String startTime, String endTime) {
        IPage<Inform> informList = informService.getInformList(request, inform,startTime,endTime);
        List<Inform> records = informList.getRecords();
        for (Inform i : records
        ) {
            i.setKey(i.getId());
        }
        return new JiebaoResponse().data(this.getDataTable(informList));
    }

    @DeleteMapping("/{ids}")
    @Log("删除通知公告")
    @ApiOperation(value = "批量删除", notes = "批量删除", response = JiebaoResponse.class, httpMethod = "DELETE")
    @Transactional(rollbackFor = Exception.class)
    public JiebaoResponse delete(@PathVariable String[] ids) throws JiebaoException {
        try {
            Arrays.stream(ids).forEach(id -> {
                informService.removeById(id);
            });
        } catch (Exception e) {
            throw new JiebaoException("删除失败");
        }
        return new JiebaoResponse().message("删除成功");
    }

    @PostMapping
    @Log("新增通知公告")
    @ApiOperation(value = "新增通知公告", notes = "新增通知公告", response = JiebaoResponse.class, httpMethod = "POST")
    @Transactional(rollbackFor = Exception.class)
    public JiebaoResponse addInform(@Valid Inform inform) {
        inform.setStatus(1);
        informService.save(inform);
        return new JiebaoResponse().message("成功");
    }

    @PutMapping
    @Log("修改通知公告")
    @Transactional(rollbackFor = Exception.class)
    @ApiOperation(value = "修改", notes = "修改", response = JiebaoResponse.class, httpMethod = "PUT")
    public void updateAddress(@Valid Inform inform) throws JiebaoException {
        try {

            this.informService.updateByKey(inform);
        } catch (Exception e) {
            message = "修改通讯录失败";
            log.error(message, e);
            throw new JiebaoException(message);
        }
    }

   /* @PostMapping(value = "/excel")
    @ApiOperation(value = "导出", notes = "导出", httpMethod = "POST")
    //@RequiresPermissions("inform:export")
    public void export(Inform inform, QueryRequest request, HttpServletResponse response) throws JiebaoException {
        try {
            List<Inform> informs = this.informService.getInformLists(inform, request);
            ExcelKit.$Export(Inform.class, response).downXlsx(informs, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new JiebaoException(message);
        }
    }*/





    @GetMapping("/revoke/{informIds}")
    @Log("撤销通知公告")
    @Transactional(rollbackFor = Exception.class)
    @ApiOperation(value = "撤销通知公告", notes = "撤销通知公告", response = JiebaoResponse.class, httpMethod = "GET")
    public JiebaoResponse revoke(@PathVariable String[] informIds) throws JiebaoException {
        try {
            if (informIds != null) {
                Arrays.stream(informIds).forEach(informId -> {
                    //状态status改为2
                        informService.revoke(informId);
                });
                return new JiebaoResponse().message("撤销通知公告成功");
            }

        } catch (Exception e) {
            message = "撤销通知公告失败";
            log.error(message, e);
            throw new JiebaoException(message);
        }
            return new JiebaoResponse().message("通知公告撤销失败");

    }


    @GetMapping("/release/{informIds}")
    @Log("发布通知公告")
    @Transactional(rollbackFor = Exception.class)
    @ApiOperation(value = "发布通知公告", notes = "发布通知公告", response = JiebaoResponse.class, httpMethod = "GET")
    public JiebaoResponse release(@PathVariable String[] informIds) throws JiebaoException {
        try {
            if (informIds != null) {
                Arrays.stream(informIds).forEach(informId -> {
                    //状态status改为3
                        informService.release(informId);
                });
                return new JiebaoResponse().message("发布通知公告成功");
            }
        } catch (Exception e) {
            message = "发布通知公告失败";
            log.error(message, e);
            throw new JiebaoException(message);
        }
        return new JiebaoResponse().message("发布通知公告失败");
    }
}
