package com.jiebao.platfrom.railway.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiebao.platfrom.common.annotation.Log;
import com.jiebao.platfrom.common.authentication.JWTUtil;
import com.jiebao.platfrom.common.controller.BaseController;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.domain.QueryRequest;
import com.jiebao.platfrom.common.exception.JiebaoException;
import com.jiebao.platfrom.railway.dao.ExchangeMapper;
import com.jiebao.platfrom.railway.dao.ExchangeUserMapper;
import com.jiebao.platfrom.railway.domain.Address;
import com.jiebao.platfrom.railway.domain.Exchange;
import com.jiebao.platfrom.railway.domain.ExchangeFile;
import com.jiebao.platfrom.railway.domain.Inform;
import com.jiebao.platfrom.railway.service.ExchangeFileService;
import com.jiebao.platfrom.railway.service.ExchangeService;
import com.jiebao.platfrom.railway.service.ExchangeUserService;
import com.jiebao.platfrom.system.domain.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.File;
import java.util.*;


/**
 * @author yf
 */
@Slf4j
@RestController
@RequestMapping(value = "/exchange")
@Api(tags = "信息互递")   //swagger2 api文档说明示例
public class ExchangeController extends BaseController {


    private String message;

    @Autowired
    private ExchangeService exchangeService;

    @Autowired
    private ExchangeUserService exchangeUserService;

    @Autowired
    private ExchangeFileService exchangeFileService;

    @Autowired
    private ExchangeMapper exchangeMapper;

    /**
     * 创建一条信息互递
     */
    @PostMapping("/{sendUserIds}")
    @ApiOperation(value = "创建一条信息互递", notes = "创建一条信息互递", response = JiebaoResponse.class, httpMethod = "POST")
    public void creatExchange(@Valid Exchange exchange, @PathVariable String[] sendUserIds) throws JiebaoException {
        try {
            String username = JWTUtil.getUsername((String) SecurityUtils.getSubject().getPrincipal());
            //把信息互递先设置为未发送状态
            exchange.setStatus("1");
            if (username != null) {
                exchange.setCreatUser(username);
            }
            boolean save = exchangeService.save(exchange);
            if (save) {
                Arrays.stream(sendUserIds).forEach(sendUserId -> {
                    //把要发送的用户保存到数据库
                    exchangeUserService.saveByUserId(exchange.getId(), sendUserId);
                });
            }
        } catch (Exception e) {
            message = "创建信息互递失败";
            log.error(message, e);
            throw new JiebaoException(message);
        }
    }

    @GetMapping("/release/{exchangeIds}")
    @Log("批量发布信息互递")
    @Transactional(rollbackFor = Exception.class)
    @ApiOperation(value = "批量发布信息互递", notes = "批量发布信息互递", response = JiebaoResponse.class, httpMethod = "GET")
    public JiebaoResponse release(@PathVariable String[] exchangeIds) throws JiebaoException {
        try {
            if (exchangeIds != null) {
                Arrays.stream(exchangeIds).forEach(exchangeId -> {
                    //状态status改为3
                    exchangeMapper.release(exchangeId);
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


    @DeleteMapping("/{exchangeIds}")
    @ApiOperation(value = "删除信息", notes = "删除信息", response = JiebaoResponse.class, httpMethod = "DELETE")
    public void deleteExchange(@PathVariable String[] exchangeIds) throws JiebaoException {
        try {
            Arrays.stream(exchangeIds).forEach(exchangeId -> {
                List<ExchangeFile> exchangeFiles = exchangeFileService.getByExchangeId(exchangeId);
                Exchange byId = exchangeService.getById(exchangeId);
                //未发送状态，删掉文件，删除接收人，删除该信息本体
                if ("1".equals(byId.getStatus())) {
                    for (ExchangeFile exchangeFile : exchangeFiles
                    ) {
                        File file = new File(exchangeFile.getUrl());
                        if (file.exists()) {
                            file.delete();
                        }
                    }
                    exchangeUserService.deleteByExchangeId(exchangeId);
                    exchangeService.removeById(exchangeId);
                    //已发布状态，只把状态改为4即可，没有2撤回
                } else if ("3".equals(byId.getStatus())) {
                    exchangeMapper.updateStatus(exchangeId);
                }
            });
        } catch (Exception e) {
            message = "删除发件箱失败";
            log.error(message, e);
            throw new JiebaoException(message);
        }
    }

    @PutMapping
    @ApiOperation(value = "修改未发送的信息互递", notes = "修改未发送的信息互递", httpMethod = "PUT")
    @Transactional(rollbackFor = Exception.class)
    public void updateExchange(@Valid Exchange exchange) throws JiebaoException {
        try {
            this.exchangeService.updateById(exchange);
        } catch (Exception e) {
            message = "修改信息互递失败";
            log.error(message, e);
            throw new JiebaoException(message);
        }
    }

    @GetMapping
    @ApiOperation(value = "分页查询", notes = "查询分页数据", response = JiebaoResponse.class, httpMethod = "GET")
    public JiebaoResponse getExchangeList(QueryRequest request, Exchange exchange, String startTime, String endTime) {
        IPage<Exchange> exchangeList = exchangeService.getExchangeList(request, exchange, startTime, endTime);
        return new JiebaoResponse().data(this.getDataTable(exchangeList));
    }


}
