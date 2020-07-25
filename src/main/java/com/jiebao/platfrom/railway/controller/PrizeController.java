package com.jiebao.platfrom.railway.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiebao.platfrom.common.authentication.JWTUtil;
import com.jiebao.platfrom.common.controller.BaseController;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.domain.QueryRequest;
import com.jiebao.platfrom.common.exception.JiebaoException;
import com.jiebao.platfrom.railway.domain.Exchange;
import com.jiebao.platfrom.railway.domain.ExchangeFile;
import com.jiebao.platfrom.railway.domain.Prize;
import com.jiebao.platfrom.railway.service.PrizeService;
import com.jiebao.platfrom.railway.service.PrizeUserService;
import io.swagger.annotations.Api;
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

    /**
     * 创建一条一事一奖
     */
    @PostMapping("/{sendUsers}")
    @ApiOperation(value = "创建一条一事一奖", notes = "创建一条一事一奖", response = JiebaoResponse.class, httpMethod = "POST")
    public void creatPrize(@Valid Prize prize, @PathVariable String[] sendUsers) throws JiebaoException {
        try {
           /* String username = JWTUtil.getUsername((String) SecurityUtils.getSubject().getPrincipal());
            if (username != null) {
                prize.setCreatUser(username);
            }*/
            boolean save = prizeService.save(prize);
            if (save) {
                Arrays.stream(sendUsers).forEach(sendUser -> {
                    //把要发送的用户保存到数据库
                    prizeUserService.saveByUser(prize.getId(), sendUser);
                });
            }
            message ="创建成功";
            throw new JiebaoException(message);
        } catch (Exception e) {
            message = "创建一事一奖失败";
            log.error(message, e);
            throw new JiebaoException(message);
        }
    }


    @DeleteMapping("/{prizeIds}")
    @ApiOperation(value = "删除一事一奖", notes = "删除一事一奖", response = JiebaoResponse.class, httpMethod = "DELETE")
    public void deletePrize(@PathVariable String[] prizeIds) throws JiebaoException {
        try {
            Arrays.stream(prizeIds).forEach(prizeId -> {
                //删掉文件（暂时未做文件），删除接收人，删除该信息本体
                prizeUserService.deleteByPrizeId(prizeId);
                prizeService.removeById(prizeId);
            });
        } catch (Exception e) {
            message = "删除一事一奖失败";
            log.error(message, e);
            throw new JiebaoException(message);
        }
    }

    @PutMapping
    @ApiOperation(value = "修改一事一奖", notes = "修改一事一奖", httpMethod = "PUT")
    @Transactional(rollbackFor = Exception.class)
    public void updatePrize(@Valid Prize prize) throws JiebaoException {
        try {
            this.prizeService.updateById(prize);
        } catch (Exception e) {
            message = "修改信息互递失败";
            log.error(message, e);
            throw new JiebaoException(message);
        }
    }


    @GetMapping
    @ApiOperation(value = "分页查询", notes = "查询分页数据", response = JiebaoResponse.class, httpMethod = "GET")
    public JiebaoResponse getPrizeList(QueryRequest request, Prize prize, String startTime, String endTime) {
        IPage<Prize> prizeList = prizeService.getPrizeList(request, prize, startTime, endTime);
        return new JiebaoResponse().data(this.getDataTable(prizeList));
    }


}
