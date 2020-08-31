package com.jiebao.platfrom.railway.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiebao.platfrom.common.annotation.Log;
import com.jiebao.platfrom.common.authentication.JWTUtil;
import com.jiebao.platfrom.common.controller.BaseController;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.domain.QueryRequest;
import com.jiebao.platfrom.common.exception.JiebaoException;
import com.jiebao.platfrom.railway.dao.BriefingMapper;
import com.jiebao.platfrom.railway.dao.BriefingUserMapper;
import com.jiebao.platfrom.railway.domain.Briefing;
import com.jiebao.platfrom.railway.domain.BriefingCount;
import com.jiebao.platfrom.railway.domain.BriefingUser;
import com.jiebao.platfrom.railway.service.BriefingCountService;
import com.jiebao.platfrom.railway.service.BriefingService;
import com.jiebao.platfrom.railway.service.BriefingUserService;
import com.jiebao.platfrom.system.dao.FileMapper;
import com.jiebao.platfrom.system.domain.Dept;
import com.jiebao.platfrom.system.domain.User;
import com.jiebao.platfrom.system.service.DeptService;
import com.jiebao.platfrom.system.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
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
@RequestMapping(value = "/briefing")
@Api(tags = "railWay-护路简报")   //swagger2 api文档说明示例
public class BriefingController extends BaseController {


    private String message;

    @Autowired
    private BriefingService briefingService;

    @Autowired
    private BriefingUserService briefingUserService;


    @Autowired
    private BriefingMapper briefingMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private BriefingUserMapper briefingUserMapper;

    @Autowired
    private FileMapper fileMapper;

    @Autowired
    private DeptService deptService;

    @Autowired
    private BriefingCountService briefingCountService;


    /**
     * 创建一条护路简报
     */
    @PostMapping("/creat")
    @ApiOperation(value = "创建一条护路简报或创建并发送(修改)", notes = "创建一条护路简报或创建并发送(修改)", response = JiebaoResponse.class, httpMethod = "POST")
    public JiebaoResponse creatBriefing(@Valid Briefing briefing, String[] sendUserIds, String[] fileIds,List<BriefingCount> briefingCounts) throws JiebaoException {
        try {
            String username = JWTUtil.getUsername((String) SecurityUtils.getSubject().getPrincipal());
            if (username != null) {
                briefing.setCreatUser(username);
            }
            if (briefing.getStatus() == 1) {
                boolean save = briefingService.saveOrUpdate(briefing);
                Arrays.stream(fileIds).forEach(fileId -> {
                    fileMapper.updateByFileId(fileId, briefing.getId());
                });
                briefingUserService.deleteByBriefingId(briefing.getId());
                if (save) {
                    Arrays.stream(sendUserIds).forEach(sendUserId -> {
                        //把要发送的用户保存到数据库
                        User byId = userService.getById(sendUserId);
                        briefingUserService.saveByUserId(briefing.getId(), sendUserId, byId.getUsername());
                    });
                }
                for (BriefingCount b:briefingCounts
                ) {
                    b.setBriefingId(briefing.getId());
                    briefingCountService.save(b);
                }
                return new JiebaoResponse().message("创建一条护路简报成功");
            } else if (briefing.getStatus() == 3) {
                boolean save = briefingService.saveOrUpdate(briefing);
                Arrays.stream(fileIds).forEach(fileId -> {
                    fileMapper.updateByFileId(fileId, briefing.getId());
                });
                if (save) {
                    Arrays.stream(sendUserIds).forEach(sendUserId -> {
                        //把要发送的用户保存到数据库
                        User byId = userService.getById(sendUserId);
                        briefingUserService.saveByUserId(briefing.getId(), sendUserId, byId.getUsername());
                    });
                }
                briefingMapper.releaseSave(briefing.getId());
                briefingUserMapper.setCreatTime(briefing.getId());
                return new JiebaoResponse().message("创建并发布一条护路简报成功");
            }
            return new JiebaoResponse().message("系统错误");
        } catch (Exception e) {
            message = "创建护路简报失败";
            log.error(message, e);
            throw new JiebaoException("创建一条护路简报失败");
        }
    }

    @GetMapping("/release/{briefingIds}")
    @Log("批量发布护路简报")
    @Transactional(rollbackFor = Exception.class)
    @ApiOperation(value = "批量发布护路简报", notes = "批量发布护路简报", response = JiebaoResponse.class, httpMethod = "GET")
    public JiebaoResponse release(@PathVariable String[] briefingIds) throws JiebaoException {
        try {
            if (briefingIds != null) {
                Arrays.stream(briefingIds).forEach(briefingId -> {
                    //状态status改为3
                    briefingMapper.release(briefingId);
                    briefingUserMapper.setCreatTime(briefingId);
                });
                return new JiebaoResponse().message("发布护路简报成功");
            }
        } catch (Exception e) {
            message = "发布护路简报失败";
            log.error(message, e);
            throw new JiebaoException(message);
        }
        return new JiebaoResponse().message("发布护路简报失败");
    }


    @DeleteMapping("/{briefingIds}")
    @ApiOperation(value = "批量删除信息（完全删除未发送的，假删除已发送的）(发件箱)", notes = "批量删除信息（完全删除未发送的，假删除已发送的）(发件箱)", response = JiebaoResponse.class, httpMethod = "DELETE")
    public JiebaoResponse deleteBriefing(@PathVariable String[] briefingIds) throws JiebaoException {
        try {
            Arrays.stream(briefingIds).forEach(briefingId -> {
                Briefing byId = briefingService.getById(briefingId);
                //未发送状态，删掉文件，删除接收人，删除该信息本体
                if (byId.getStatus() == 1) {
                    briefingUserService.deleteByBriefingId(briefingId);
                    briefingService.removeById(briefingId);
                    //已发布状态，只把状态改为4即可，没有2撤回
                } else if (byId.getStatus() == 3) {
                    briefingMapper.updateStatus(briefingId);
                }
            });
            return new JiebaoResponse().message("批量删除信息成功");
        } catch (Exception e) {
            message = "删除发件箱失败";
            log.error(message, e);
            throw new JiebaoException("批量删除信息失败");
        }
    }

    @PutMapping("{sendUserIds}")
    @ApiOperation(value = "修改未发送的护路简报", notes = "修改未发送的护路简报", httpMethod = "PUT")
    @Transactional(rollbackFor = Exception.class)
    public JiebaoResponse updateBriefing(@Valid Briefing briefing, @PathVariable String[] sendUserIds) throws JiebaoException {
        try {
            briefingService.updateById(briefing);
            //先删除掉原有发送人
            boolean b = briefingUserService.deleteByBriefingId(briefing.getId());
            //重新添加
            if (b) {
                Arrays.stream(sendUserIds).forEach(sendUserId -> {
                    //把要发送的用户保存到数据库
                    User byId = userService.getById(sendUserId);
                    briefingUserService.saveByUserId(briefing.getId(), sendUserId, byId.getUsername());
                });
                return new JiebaoResponse().message("修改未发送的护路简报成功");
            } else {
                return new JiebaoResponse().message("修改未发送的护路简报失败");
            }
        } catch (Exception e) {
            message = "修改护路简报失败";
            log.error(message, e);
            throw new JiebaoException("修改失败");
        }
    }

    @GetMapping
    @ApiOperation(value = "分页查询（查询未发送和已发送的）", notes = "查询分页数据（查询未发送和已发送的）", response = JiebaoResponse.class, httpMethod = "GET")
    public JiebaoResponse getBriefingList(QueryRequest request, Briefing briefing, String startTime, String endTime) {
        IPage<Briefing> briefingList = briefingService.getBriefingList(request, briefing, startTime, endTime);
        return new JiebaoResponse().data(this.getDataTable(briefingList));
    }

    @GetMapping("/forCheck")
    @ApiOperation(value = "分页查询（查询未发送和已发送的给年度考核）", notes = "查询分页数据（查询未发送和已发送的给年度考核）", response = JiebaoResponse.class, httpMethod = "GET")
    public JiebaoResponse getBriefingListForCheck(QueryRequest request, Briefing briefing, String id, String startTime, String endTime) {
        IPage<Briefing> briefingList = briefingService.getBriefingListForCheck(request, briefing, id, startTime, endTime);
        return new JiebaoResponse().data(this.getDataTable(briefingList));
    }


    @GetMapping("/inbox")
    @ApiOperation(value = "分页查询（查询收件箱）", notes = "查询分页数据（查询收件箱）", response = JiebaoResponse.class, httpMethod = "GET")
    public JiebaoResponse getBriefingInboxList(QueryRequest request, Briefing briefing, String startTime, String endTime) {
        IPage<Briefing> briefingList = briefingService.getBriefingInboxList(request, briefing, startTime, endTime);
        List<Briefing> records = briefingList.getRecords();
        for (Briefing e : records
        ) {
            String username = JWTUtil.getUsername((String) SecurityUtils.getSubject().getPrincipal());
            User byName = userService.findByName(username);
            BriefingUser briefingUser = briefingUserMapper.getIsRead(byName.getUserId(), e.getId());
            if (briefingUser != null) {
                e.setIsRead(briefingUser.getIsRead());
            }
        }
        return new JiebaoResponse().data(this.getDataTable(briefingList));
    }


    @DeleteMapping("/inbox/{briefingIds}")
    @ApiOperation(value = "批量删除信息（收件箱）", notes = "批量删除信息（收件箱）", response = JiebaoResponse.class, httpMethod = "DELETE")
    public JiebaoResponse deleteInboxBriefing(@PathVariable String[] briefingIds) throws JiebaoException {
        try {
            String username = JWTUtil.getUsername((String) SecurityUtils.getSubject().getPrincipal());
            User byName = userService.findByName(username);
            Arrays.stream(briefingIds).forEach(briefingId -> {
                briefingUserService.removeBySendUserId(byName.getUserId(), briefingId);
            });
            return new JiebaoResponse().message("批量删除信息成功");
        } catch (Exception e) {
            log.error(message, e);
            throw new JiebaoException("批量删除信息失败");
        }
    }


    @GetMapping(value = "/getInfoById/{briefingId}")
    @ApiOperation(value = "根据ID查信息info", notes = "根据ID查信息info", response = JiebaoResponse.class, httpMethod = "GET")
    public Briefing getInfoById(@PathVariable String briefingId) {
        Briefing byId = briefingService.getById(briefingId);
        Map<String, Object> columnMap = new HashMap<>();
        //列briefing_id为数据库中的列名，不是实体类中的属性名
        columnMap.put("briefing_id", briefingId);
        List listId = new ArrayList();
        List listName = new ArrayList();
        //    List listDeptName = new ArrayList();
        List<BriefingUser> briefingUsers = briefingUserMapper.selectByMap(columnMap);
        for (BriefingUser eu : briefingUsers
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
        map.put("ref_id", briefingId);
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
    public JiebaoResponse getUserInfo(QueryRequest request, BriefingUser briefingUser) {
        IPage<BriefingUser> briefingUserList = briefingUserService.getBriefingUserList(request, briefingUser);
        List<BriefingUser> records = briefingUserList.getRecords();
        for (BriefingUser e : records
        ) {
            User byId = userService.getById(e.getSendUserId());
            Dept byDeptId = deptService.getById(byId.getDeptId());
            e.setDeptName(byDeptId.getDeptName());
        }
        HashMap<String, Object> map = new HashMap<>();
        int zero = briefingUserMapper.countByIsReadZero(briefingUser.getBriefingId());
        int one = briefingUserMapper.countByIsReadOne(briefingUser.getBriefingId());
        map.put("zero", zero);
        map.put("one", one);
        Map<String, Object> rspData = new HashMap<>();
        rspData.put("rows", briefingUserList.getRecords());
        rspData.put("isRead", map);
        rspData.put("total", briefingUserList.getTotal());
        return new JiebaoResponse().data(rspData);
    }


/*
    @PostMapping("/countBriefing")
    @ApiOperation(value = "统计", notes = "统计", response = JiebaoResponse.class, httpMethod = "POST")
    public JiebaoResponse countBriefing(@Valid List<BriefingCount> briefingCounts) throws JiebaoException {
        try {
            for (BriefingCount b:briefingCounts
                 ) {
                briefingCountService.save(b);
            }
            return new JiebaoResponse().okMessage("添加成功");
        } catch (Exception e) {
            throw  new JiebaoException("添加失败");
        }

    }*/


}
