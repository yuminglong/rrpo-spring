package com.jiebao.platfrom.railway.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiebao.platfrom.common.authentication.JWTUtil;
import com.jiebao.platfrom.common.domain.JiebaoConstant;
import com.jiebao.platfrom.common.domain.QueryRequest;
import com.jiebao.platfrom.common.utils.SortUtil;
import com.jiebao.platfrom.railway.dao.PrizeOrderMapper;
import com.jiebao.platfrom.railway.dao.PrizeUserMapper;
import com.jiebao.platfrom.railway.domain.Exchange;
import com.jiebao.platfrom.railway.domain.ExchangeUser;
import com.jiebao.platfrom.railway.domain.Prize;
import com.jiebao.platfrom.railway.dao.PrizeMapper;
import com.jiebao.platfrom.railway.domain.PrizeUser;
import com.jiebao.platfrom.railway.service.PrizeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiebao.platfrom.system.dao.UserMapper;
import com.jiebao.platfrom.system.domain.User;
import com.jiebao.platfrom.system.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 一事一奖内容表 服务实现类
 * </p>
 *
 * @author yf
 * @since 2020-07-24
 */
@Slf4j
@Service("PrizeService")
public class PrizeServiceImpl extends ServiceImpl<PrizeMapper, Prize> implements PrizeService {


    @Autowired
    PrizeMapper prizeMapper;

    @Autowired
    PrizeUserMapper prizeUserMapper;

    @Autowired
    UserService userService;

    @Override
    public IPage<Prize> getPrizeList(QueryRequest request, Prize prize, String startTime, String endTime) {
        QueryWrapper<Prize> queryWrapper = new QueryWrapper<>();
        String username = JWTUtil.getUsername((String) SecurityUtils.getSubject().getPrincipal());
        queryWrapper.lambda().ne(Prize::getStatus, 4);
        if (username != null) {
            queryWrapper.lambda().eq(Prize::getCreatUser, username);
        }
        if (StringUtils.isNotBlank(prize.getTitle())) {
            queryWrapper.lambda().like(Prize::getTitle, prize.getTitle());
        }
        if (StringUtils.isNotBlank(prize.getContent())){
            queryWrapper.lambda().like(Prize::getContent, prize.getContent());
        }
        if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
            queryWrapper.lambda().ge(Prize::getCreatTime, startTime).le(Prize::getCreatTime, endTime);
        }
        Page<Prize> page = new Page<>(request.getPageNum(), request.getPageSize());
        SortUtil.handleWrapperSort(request, queryWrapper, "creatTime", JiebaoConstant.ORDER_DESC, true);
        return this.baseMapper.selectPage(page, queryWrapper);
    }

    @Override
    public IPage<Prize> getPrizeInboxList(QueryRequest request, Prize prize, String startTime, String endTime) {
        QueryWrapper<Prize> queryWrapper = new QueryWrapper<>();
        String username = JWTUtil.getUsername((String) SecurityUtils.getSubject().getPrincipal());
        queryWrapper.lambda().and(wrapper -> wrapper.eq(Prize::getStatus, 3).or().eq(Prize::getStatus, 4));
        if (StringUtils.isNotBlank(username)) {
            User byName = userService.findByName(username);
            Map<String,Object> map = new HashMap<>();
            map.put("send_user",byName.getUserId());
            List<PrizeUser> prizeUsers = prizeUserMapper.selectByMap(map);
            ArrayList<String> prizeUserIds = new ArrayList<>();
            for (PrizeUser prizeUser : prizeUsers
            ) {
                String prizeId = prizeUser.getPrizeId();
                prizeUserIds.add(prizeId);
            }
            if (prizeUserIds.size() > 0) {
                queryWrapper.lambda().in(Prize::getId, prizeUserIds);
            }
        }
        if (StringUtils.isNotBlank(prize.getTitle())) {
            queryWrapper.lambda().like(Prize::getTitle, prize.getTitle());
        }
        if (StringUtils.isNotBlank(prize.getContent())){
            queryWrapper.lambda().like(Prize::getContent, prize.getContent());
        }
        if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
            queryWrapper.lambda().ge(Prize::getCreatTime, startTime).le(Prize::getCreatTime, endTime);
        }
        Page<Prize> page = new Page<>(request.getPageNum(), request.getPageSize());
        SortUtil.handleWrapperSort(request, queryWrapper, "releaseTime", JiebaoConstant.ORDER_DESC, true);
        return this.baseMapper.selectPage(page, queryWrapper);
    }
}
