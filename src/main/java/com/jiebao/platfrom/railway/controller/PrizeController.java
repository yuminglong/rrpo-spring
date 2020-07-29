package com.jiebao.platfrom.railway.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiebao.platfrom.common.authentication.JWTUtil;
import com.jiebao.platfrom.common.controller.BaseController;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.domain.QueryRequest;
import com.jiebao.platfrom.common.exception.JiebaoException;
import com.jiebao.platfrom.railway.dao.PrizeMapper;
import com.jiebao.platfrom.railway.domain.Exchange;
import com.jiebao.platfrom.railway.domain.ExchangeFile;
import com.jiebao.platfrom.railway.domain.Prize;
import com.jiebao.platfrom.railway.service.PrizeService;
import com.jiebao.platfrom.railway.service.PrizeUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 一事一奖内容表
 * </p>
 *
 * @author yf
 * @since 2020-07-24
 */
@Slf4j
@RestController
@RequestMapping("/prize")
@Api(tags = "一事一奖")   //swagger2 api文档说明示例
public class PrizeController extends BaseController {


    private String message;


    @Autowired
    private PrizeUserService prizeUserService;

    @Autowired
    private PrizeService prizeService;

    @Autowired
    private PrizeMapper prizeMapper;

    /**
     * 创建一条一事一奖
     */

    @ApiImplicitParams({
            @ApiImplicitParam(name = "sendDepts", value = "要发送的组织机构（多个）")
    })
    @PostMapping("/{sendDepts}")
    @ApiOperation(value = "创建一条一事一奖", notes = "创建一条一事一奖", response = JiebaoResponse.class, httpMethod = "POST")
    public JiebaoResponse creatPrize(@Valid Prize prize, @PathVariable String[] sendDepts) throws JiebaoException {
        try {
           /* String username = JWTUtil.getUsername((String) SecurityUtils.getSubject().getPrincipal());
            if (username != null) {
                prize.setCreatUser(username);
            }*/
            //初始化为1，未发送状态
            prize.setStatus("1");
            boolean save = prizeService.save(prize);
            if (save) {
                Arrays.stream(sendDepts).forEach(sendDept -> {
                    //把要发送的用户保存到数据库
                    prizeUserService.saveByDept(prize.getId(), sendDept);
                });
            }
            return new JiebaoResponse().message("创建一条一事一奖成功");

        } catch (Exception e) {
            message = "创建一事一奖失败";
            log.error(message, e);
            return new JiebaoResponse().message("创建一条一事一奖失败");
        }
    }


    /**
     * 批量发布一事一奖
     */
    @PutMapping("/{prizeIds}")
    @ApiOperation(value = "批量发布一事一奖", notes = "批量发布一事一奖", response = JiebaoResponse.class, httpMethod = "PUT")
    public JiebaoResponse creatPrize(@PathVariable String[] prizeIds) throws JiebaoException {
        try {
            Arrays.stream(prizeIds).forEach(prizeId -> {
                //把status改为3
                prizeMapper.release(prizeId);
            });
            //发送成功
            return new JiebaoResponse().message("发布一事一奖成功");
        } catch (Exception e) {
            message = "创建一事一奖失败";
            log.error(message, e);
            return new JiebaoResponse().message("发布一事一奖失败");
        }
    }


    @ApiImplicitParams({
            @ApiImplicitParam(name = "prizeIds", value = "一事一奖的id")
    })
    @DeleteMapping("/{prizeIds}")
    @ApiOperation(value = "删除一事一奖（包括未发布的，已发布的，已发布的并不会真正删除，但是查询会显示不出）", notes = "删除一事一奖（包括未发布的，已发布的，已发布的并不会真正删除，但是查询会显示不出）", response = JiebaoResponse.class, httpMethod = "DELETE")
    public JiebaoResponse deletePrize(@PathVariable String[] prizeIds) throws JiebaoException {
        try {
            Arrays.stream(prizeIds).forEach(prizeId -> {
                //不能直接删掉文件（暂时未做文件），不能删除接收人，不能删除该信息本体，直接改状态 status为4

                Prize byId = prizeService.getById(prizeId);
                //1为未发送状态
                if ("1".equals(byId.getStatus())) {
                    //删除接收的组织机构
                    prizeUserService.deleteByPrizeId(prizeId);
                    //删除内容本体（文件还没加哦）
                    prizeService.removeById(prizeId);
                    //3为已发送状态，只需改状态为4
                } else if ("3".equals(byId.getStatus())) {
                    prizeUserService.ByPrizeId(prizeId);
                }
            });
            return new JiebaoResponse().message("删除一事一奖成功");
        } catch (Exception e) {
            message = "删除一事一奖失败";
            log.error(message, e);
            return new JiebaoResponse().message("删除一事一奖失败");
        }
    }


    @ApiImplicitParams({
            @ApiImplicitParam(name = "sendDepts", value = "发送的组织机构（多个）")
    })
    @PutMapping("{sendDepts}")
    @ApiOperation(value = "修改未发送的一事一奖（已发布的不能修改）", notes = "修改发送的一事一奖（已发布的不能修改）", httpMethod = "PUT")
    @Transactional(rollbackFor = Exception.class)
    public JiebaoResponse updatePrize(@Valid Prize prize, @PathVariable String[] sendDepts) throws JiebaoException {
        try {
            this.prizeService.updateById(prize);
            //先删除掉原本的接收组织机构
            prizeUserService.deleteByPrizeId(prize.getId());
            //再重新获取，把要发送的用户保存到数据库
            Arrays.stream(sendDepts).forEach(sendDept -> {
                prizeUserService.saveByDept(prize.getId(), sendDept);
            });
            return new JiebaoResponse().message("修改未发送的一事一奖成功");
        } catch (Exception e) {
            message = "修改一事一奖失败";
            log.error(message, e);
            return new JiebaoResponse().message("修改未发送的一事一奖失败");
        }
    }


    @GetMapping
    @ApiOperation(value = "分页查询发件箱（未发送的和已发送的）", notes = "分页查询发件箱（未发送的和已发送的）", response = JiebaoResponse.class, httpMethod = "GET")
    public JiebaoResponse getPrizeList(QueryRequest request, Prize prize, String startTime, String endTime) {
        IPage<Prize> prizeList = prizeService.getPrizeList(request, prize, startTime, endTime);
        return new JiebaoResponse().data(this.getDataTable(prizeList));
    }




}
