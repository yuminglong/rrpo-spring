package com.jiebao.platfrom.railway.controller;


import com.jiebao.platfrom.common.annotation.Log;
import com.jiebao.platfrom.common.controller.BaseController;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.exception.JiebaoException;
import com.jiebao.platfrom.railway.domain.Address;
import com.jiebao.platfrom.railway.domain.Exchange;
import com.jiebao.platfrom.railway.domain.ExchangeFile;
import com.jiebao.platfrom.railway.service.ExchangeFileService;
import com.jiebao.platfrom.railway.service.ExchangeService;
import com.jiebao.platfrom.railway.service.ExchangeUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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

    /**
     * 创建一条信息互递
     */
    @PostMapping("/{sendUserIds}")
    @ApiOperation(value = "创建一条信息互递", notes = "创建一条信息互递", response = JiebaoResponse.class, httpMethod = "POST")
    public void creatExchange(@Valid Exchange exchange, @PathVariable String[] sendUserIds) throws JiebaoException {
        try {
            //把信息互递先设置为未发送状态
            exchange.setStatus(1);
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

    @DeleteMapping("/{exchangeIds}")
    @ApiOperation(value = "删除未发布的信息", notes = "删除保存后未发送的信息", response = JiebaoResponse.class, httpMethod = "DELETE")
    public void deleteExchange(@PathVariable String[] exchangeIds) throws JiebaoException {
        try {
            Arrays.stream(exchangeIds).forEach(exchangeId -> {
                List<ExchangeFile> exchangeFiles = exchangeFileService.getByExchangeId(exchangeId);
                for (ExchangeFile exchangeFile : exchangeFiles
                ) {
                    File file = new File(exchangeFile.getUrl());
                    if (file.exists()) {
                        file.delete();
                    }
                }
                exchangeUserService.deleteByExchangeId(exchangeId);
               exchangeUserService.removeById(exchangeId);
            });
        } catch (Exception e) {
            message = "删除发件箱失败";
            log.error(message, e);
            throw new JiebaoException(message);
        }
    }

    @PutMapping
    @ApiOperation(value = "修改未发布的信息互递", notes = "修改未发布的信息互递", httpMethod = "PUT")
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


}
