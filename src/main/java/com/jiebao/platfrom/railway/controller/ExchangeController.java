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
import com.jiebao.platfrom.railway.domain.*;
import com.jiebao.platfrom.railway.service.ExchangeFileService;
import com.jiebao.platfrom.railway.service.ExchangeService;
import com.jiebao.platfrom.railway.service.ExchangeUserService;
import com.jiebao.platfrom.system.dao.FileMapper;
import com.jiebao.platfrom.system.domain.Dept;
import com.jiebao.platfrom.system.domain.User;
import com.jiebao.platfrom.system.service.DeptService;
import com.jiebao.platfrom.system.service.FileService;
import com.jiebao.platfrom.system.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.util.*;


/**
 * @author yf
 */
@Slf4j
@RestController
@RequestMapping(value = "/exchange")
@Api(tags = "railWay-信息互递")   //swagger2 api文档说明示例
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

    @Autowired
    private UserService userService;

    @Autowired
    private ExchangeUserMapper exchangeUserMapper;

    @Autowired
    private FileMapper fileMapper;

    @Autowired
    private DeptService deptService;


    /**
     * 创建一条信息互递
     */
    @PostMapping("/creat")
    @ApiOperation(value = "创建一条信息互递或创建并发送(修改)", notes = "创建一条信息互递或创建并发送(修改)", response = JiebaoResponse.class, httpMethod = "POST")
    public JiebaoResponse creatExchange(@Valid Exchange exchange, String[] sendUserIds, String[] fileIds) throws JiebaoException {
        try {
            String username = JWTUtil.getUsername((String) SecurityUtils.getSubject().getPrincipal());
            if (username != null) {
                exchange.setCreatUser(username);
            }
            if ("1".equals(exchange.getStatus())) {
                boolean save = exchangeService.saveOrUpdate(exchange);
                Arrays.stream(fileIds).forEach(fileId -> {
                    fileMapper.updateByFileId(fileId, exchange.getId());
                });
                exchangeUserService.deleteByExchangeId(exchange.getId());
                if (save) {
                    Arrays.stream(sendUserIds).forEach(sendUserId -> {
                        //把要发送的用户保存到数据库
                        User byId = userService.getById(sendUserId);
                        exchangeUserService.saveByUserId(exchange.getId(), sendUserId, byId.getUsername());
                    });
                }
                return new JiebaoResponse().message("创建一条信息互递成功");
            } else if ("3".equals(exchange.getStatus())) {
                boolean save = exchangeService.saveOrUpdate(exchange);
                Arrays.stream(fileIds).forEach(fileId -> {
                    fileMapper.updateByFileId(fileId, exchange.getId());
                });
                if (save) {
                    Arrays.stream(sendUserIds).forEach(sendUserId -> {
                        //把要发送的用户保存到数据库
                        User byId = userService.getById(sendUserId);
                        exchangeUserService.saveByUserId(exchange.getId(), sendUserId, byId.getUsername());
                    });
                }
                exchangeMapper.releaseSave(exchange.getId());
                exchangeUserMapper.setCreatTime(exchange.getId());
                return new JiebaoResponse().message("创建并发布一条信息互递成功");
            }
            return new JiebaoResponse().message("系统错误");
        } catch (Exception e) {
            message = "创建信息互递失败";
            log.error(message, e);
            throw new JiebaoException("创建一条信息互递失败");
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
                    exchangeUserMapper.setCreatTime(exchangeId);
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
    @ApiOperation(value = "批量删除信息（完全删除未发送的，假删除已发送的）(发件箱)", notes = "批量删除信息（完全删除未发送的，假删除已发送的）(发件箱)", response = JiebaoResponse.class, httpMethod = "DELETE")
    public JiebaoResponse deleteExchange(@PathVariable String[] exchangeIds) throws JiebaoException {
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
            return new JiebaoResponse().message("批量删除信息成功");
        } catch (Exception e) {
            message = "删除发件箱失败";
            log.error(message, e);
            return new JiebaoResponse().message("批量删除信息失败");
        }
    }

    @PutMapping("{sendUserIds}")
    @ApiOperation(value = "修改未发送的信息互递", notes = "修改未发送的信息互递", httpMethod = "PUT")
    @Transactional(rollbackFor = Exception.class)
    public JiebaoResponse updateExchange(@Valid Exchange exchange, @PathVariable String[] sendUserIds) throws JiebaoException {
        try {
            exchangeService.updateById(exchange);
            //先删除掉原有发送人
            boolean b = exchangeUserService.deleteByExchangeId(exchange.getId());
            //重新添加
            if (b) {
                Arrays.stream(sendUserIds).forEach(sendUserId -> {
                    //把要发送的用户保存到数据库
                    User byId = userService.getById(sendUserId);
                    exchangeUserService.saveByUserId(exchange.getId(), sendUserId, byId.getUsername());
                });
                return new JiebaoResponse().message("修改未发送的信息互递成功");
            } else {
                return new JiebaoResponse().message("修改未发送的信息互递失败");
            }
        } catch (Exception e) {
            message = "修改信息互递失败";
            log.error(message, e);
            throw new JiebaoException("修改失败");
        }
    }

    @GetMapping
    @ApiOperation(value = "分页查询（查询未发送和已发送的）", notes = "查询分页数据（查询未发送和已发送的）", response = JiebaoResponse.class, httpMethod = "GET")
    public JiebaoResponse getExchangeList(QueryRequest request, Exchange exchange, String startTime, String endTime) {
        IPage<Exchange> exchangeList = exchangeService.getExchangeList(request, exchange, startTime, endTime);
        return new JiebaoResponse().data(this.getDataTable(exchangeList));
    }

    @GetMapping("/forCheck")
    @ApiOperation(value = "分页查询（查询未发送和已发送的给年度考核）", notes = "查询分页数据（查询未发送和已发送的给年度考核）", response = JiebaoResponse.class, httpMethod = "GET")
    public JiebaoResponse getExchangeListForCheck(QueryRequest request, Exchange exchange, String id, String startTime, String endTime) {
        IPage<Exchange> exchangeList = exchangeService.getExchangeListForCheck(request, exchange, id, startTime, endTime);
        return new JiebaoResponse().data(this.getDataTable(exchangeList));
    }


    @GetMapping("/inbox")
    @ApiOperation(value = "分页查询（查询收件箱）", notes = "查询分页数据（查询收件箱）", response = JiebaoResponse.class, httpMethod = "GET")
    public JiebaoResponse getExchangeInboxList(QueryRequest request, Exchange exchange, String startTime, String endTime) {
        IPage<Exchange> exchangeList = exchangeService.getExchangeInboxList(request, exchange, startTime, endTime);
        List<Exchange> records = exchangeList.getRecords();
        for (Exchange e : records
        ) {
            String username = JWTUtil.getUsername((String) SecurityUtils.getSubject().getPrincipal());
            User byName = userService.findByName(username);
            ExchangeUser exchangeUser = exchangeUserMapper.getIsRead(byName.getUserId(), e.getId());
            e.setIsRead(exchangeUser.getIsRead());
        }
        return new JiebaoResponse().data(this.getDataTable(exchangeList));
    }


    @DeleteMapping("/inbox/{exchangeIds}")
    @ApiOperation(value = "批量删除信息（收件箱）", notes = "批量删除信息（收件箱）", response = JiebaoResponse.class, httpMethod = "DELETE")
    public JiebaoResponse deleteInboxExchange(@PathVariable String[] exchangeIds) throws JiebaoException {
        try {
            String username = JWTUtil.getUsername((String) SecurityUtils.getSubject().getPrincipal());
            User byName = userService.findByName(username);
            Arrays.stream(exchangeIds).forEach(exchangeId -> {
                exchangeUserService.removeBySendUserId(byName.getUserId(), exchangeId);
            });
            return new JiebaoResponse().message("批量删除信息成功");
        } catch (Exception e) {
            log.error(message, e);
            throw new JiebaoException("批量删除信息失败");
        }
    }

  /*  @DeleteMapping("/outbox/{exchangeIds}")
    @ApiOperation(value = "批量删除信息（收件箱）", notes = "批量删除信息（收件箱）", response = JiebaoResponse.class, httpMethod = "DELETE")
    public JiebaoResponse updateInboxExchange(@PathVariable String[] exchangeIds) throws JiebaoException {
        try {
            String username = JWTUtil.getUsername((String) SecurityUtils.getSubject().getPrincipal());
            User byName = userService.findByName(username);
            Arrays.stream(exchangeIds).forEach(exchangeId -> {
                exchangeUserService.upDateBySendUserId(byName.getUserId(),exchangeId);
            });
            return new JiebaoResponse().message("批量删除信息成功");
        } catch (Exception e) {
            log.error(message, e);
            throw new JiebaoException("批量删除信息失败");
        }
    }*/


    @GetMapping(value = "/getInfoById/{exchangeId}")
    @ApiOperation(value = "根据ID查信息info", notes = "根据ID查信息info", response = JiebaoResponse.class, httpMethod = "GET")
    public Exchange getInfoById(@PathVariable String exchangeId) {
        Exchange byId = exchangeService.getById(exchangeId);
        Map<String, Object> columnMap = new HashMap<>();
        //列exchange_id为数据库中的列名，不是实体类中的属性名
        columnMap.put("exchange_id", exchangeId);
        List listId = new ArrayList();
        List listName = new ArrayList();
    //    List listDeptName = new ArrayList();
        List<ExchangeUser> exchangeUsers = exchangeUserMapper.selectByMap(columnMap);
        for (ExchangeUser eu : exchangeUsers
        ) {
            listId.add(eu.getSendUserId());
            User user = userService.getById(eu.getSendUserId());
            listName.add(user.getUsername());
        }
        String[] array = (String[]) listId.toArray(new String[0]);
        String[] arrayName = (String[]) listName.toArray(new String[0]);
        byId.setUserId(array);
        byId.setUserName(arrayName);
        Map<String, Object> map = new HashMap<>();
        map.put("ref_id", exchangeId);
        List<com.jiebao.platfrom.system.domain.File> files = fileMapper.selectByMap(map);
        ArrayList<String> refIds = new ArrayList<>();
        for (com.jiebao.platfrom.system.domain.File f : files
        ) {
            refIds.add(f.getRefId());
        }
        byId.setRefIds(refIds);
        return byId;
    }


    @GetMapping(value = "/getUserInfo")
    @ApiOperation(value = "根据ID查接收info（发件箱）", notes = "根据ID查接收info（发件箱）", response = JiebaoResponse.class, httpMethod = "GET")
    public JiebaoResponse getUserInfo(QueryRequest request, ExchangeUser exchangeUser) {
        IPage<ExchangeUser> exchangeUserList = exchangeUserService.getExchangeUserList(request, exchangeUser);
        List<ExchangeUser> records = exchangeUserList.getRecords();
        for (ExchangeUser e: records
             ) {
            User byId = userService.getById(e.getSendUserId());
            Dept byDeptId = deptService.getById(byId.getDeptId());
          e.setDeptName(byDeptId.getDeptName());
        }
        return new JiebaoResponse().data(this.getDataTable(exchangeUserList));
    }


    @PutMapping("/receive")
    @ApiOperation(value = "意见回复或修改", notes = "意见回复或修改", httpMethod = "PUT")
    @Transactional(rollbackFor = Exception.class)
    public JiebaoResponse receive(@Valid ExchangeUser exchangeUser) throws JiebaoException {
        try {
            String username = JWTUtil.getUsername((String) SecurityUtils.getSubject().getPrincipal());
            User byName = userService.findByName(username);
            this.exchangeUserService.updateByExchangeId(byName.getUserId(), exchangeUser.getExchangeId(), exchangeUser.getOpinion());
            return new JiebaoResponse().message("意见回复或修改成功").put("status", "200");
        } catch (Exception e) {
            message = "意见回复或修改失败";
            log.error(message, e);
            throw new JiebaoException(message);
        }
    }


    @GetMapping("/getReceive")
    @ApiOperation(value = "查看回复", notes = "查看回复", httpMethod = "GET")
    @Transactional(rollbackFor = Exception.class)
    public JiebaoResponse getReceive(String exchangeId) {
        String username = JWTUtil.getUsername((String) SecurityUtils.getSubject().getPrincipal());
        User byName = userService.findByName(username);
        ExchangeUser byNameAndId = exchangeUserMapper.findByNameAndId(exchangeId, byName.getUserId());
        if (byNameAndId.getIsRead() == 0) {
            exchangeUserMapper.updateIsRead(exchangeId, byName.getUserId());
        }
        return new JiebaoResponse().data(byNameAndId).message("查看成功").put("status", "200");
    }



}
