package com.jiebao.platfrom.railway.controller;


import com.jiebao.platfrom.common.authentication.JWTUtil;
import com.jiebao.platfrom.common.controller.BaseController;
import com.jiebao.platfrom.common.exception.JiebaoException;
import com.jiebao.platfrom.railway.domain.Prize;
import com.jiebao.platfrom.railway.domain.PrizeUser;
import com.jiebao.platfrom.railway.service.PrizeUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>
 * 一事一奖推送人意见表
 * </p>
 *
 * @author yf
 * @since 2020-07-24
 */
@Slf4j
@RestController
@RequestMapping("/prizeUser")
@Api(tags = "一事一奖推送人意见")
public class PrizeUserController extends BaseController {

    private String message;
    @Autowired
    private PrizeUserService prizeUserService;

    @PutMapping("/{prizeId}")
    @ApiOperation(value = "发表或修改审核意见", notes = "发表或修改审核意见", httpMethod = "PUT")
    @Transactional(rollbackFor = Exception.class)
    public void updatePrize(@PathVariable String prizeId, String auditOpinion,String money) throws JiebaoException {
        try {
           // String userName = JWTUtil.getUsername((String) SecurityUtils.getSubject().getPrincipal());
            String userName ="sinliz";
            this.prizeUserService.updateByPrizeId(prizeId, userName, auditOpinion,money);
        } catch (Exception e) {
            message = "发表或修改审核意见失败";
            log.error(message, e);
            throw new JiebaoException(message);
        }
    }
}
