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
import com.jiebao.platfrom.system.domain.Dept;
import com.jiebao.platfrom.system.domain.User;
import com.jiebao.platfrom.system.service.DeptService;
import com.jiebao.platfrom.system.service.UserService;
import com.mchange.lang.IntegerUtils;
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

    @Autowired
    DeptService deptService;

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
        if (StringUtils.isNotBlank(prize.getNumber())){
            queryWrapper.lambda().like(Prize::getNumber, prize.getNumber());
        }
        if (StringUtils.isNotBlank(prize.getPlace())){
            queryWrapper.lambda().like(Prize::getPlace, prize.getPlace());
        }
        if (StringUtils.isNotBlank(prize.getContent())){
            queryWrapper.lambda().like(Prize::getContent, prize.getContent());
        }
        if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
            queryWrapper.lambda().ge(Prize::getCreatTime, startTime).le(Prize::getCreatTime, endTime);
        }
        if (StringUtils.isNotBlank(prize.getTypes())) {
            queryWrapper.lambda().eq(Prize::getTypes, prize.getTypes());
        }
        if (prize.getStatus()!=null){
            queryWrapper.lambda().eq(Prize::getStatus,prize.getStatus());
        }
        Page<Prize> page = new Page<>(request.getPageNum(), request.getPageSize());
        SortUtil.handleWrapperSort(request, queryWrapper, "creatTime", JiebaoConstant.ORDER_DESC, true);
        return this.baseMapper.selectPage(page, queryWrapper);
    }

    @Override
    public IPage<Prize> getPrizeInboxList(QueryRequest request, Prize prize, String startTime, String endTime) {
        QueryWrapper<Prize> queryWrapper = new QueryWrapper<>();
        String username = JWTUtil.getUsername((String) SecurityUtils.getSubject().getPrincipal());
        queryWrapper.lambda().and(wrapper -> wrapper.eq(Prize::getStatus, 3).or().eq(Prize::getStatus, 4).or().eq(Prize::getStatus, 5).or().eq(Prize::getStatus, 6).or().eq(Prize::getStatus, 7).or().eq(Prize::getStatus, 2));
        if (StringUtils.isNotBlank(username)) {
            User byName = userService.findByName(username);
            Dept dept = deptService.getById(byName.getDeptId());
            Map<String,Object> map = new HashMap<>();
            map.put("send_dept",dept.getDeptId());
            List<PrizeUser> prizeDepts = prizeUserMapper.selectByMap(map);
            ArrayList<String> prizeDeptIds = new ArrayList<>();
            for (PrizeUser prizeDept : prizeDepts
            ) {
                String prizeId = prizeDept.getPrizeId();
                prizeDeptIds.add(prizeId);
            }
            if (prizeDeptIds.size() > 0) {
                queryWrapper.lambda().in(Prize::getId, prizeDeptIds);
            }
            else {
                queryWrapper.lambda().in(Prize::getId, "111111111111111111111111111111111");
            }
        }
        if (StringUtils.isNotBlank(prize.getNumber())){
            queryWrapper.lambda().like(Prize::getNumber, prize.getNumber());
        }
        if (StringUtils.isNotBlank(prize.getTitle())) {
            queryWrapper.lambda().like(Prize::getTitle, prize.getTitle());
        }
        if (StringUtils.isNotBlank(prize.getContent())){
            queryWrapper.lambda().like(Prize::getContent, prize.getContent());
        }
        if (StringUtils.isNotBlank(prize.getPlace())){
            queryWrapper.lambda().like(Prize::getPlace, prize.getPlace());
        }
        if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
            queryWrapper.lambda().ge(Prize::getReleaseTime, startTime).le(Prize::getReleaseTime, endTime);
        }
        if (StringUtils.isNotBlank(prize.getTypes())) {
            queryWrapper.lambda().eq(Prize::getTypes, prize.getTypes());
        }
        if (prize.getStatus() !=null){
            queryWrapper.lambda().eq(Prize::getStatus,prize.getStatus());
        }
        Page<Prize> page = new Page<>(request.getPageNum(), request.getPageSize());
        SortUtil.handleWrapperSort(request, queryWrapper, "releaseTime", JiebaoConstant.ORDER_DESC, true);
        return this.baseMapper.selectPage(page, queryWrapper);
    }

    @Override
    public IPage<Prize> getBriefing(QueryRequest request, Prize prize, String startTime, String endTime) {
        QueryWrapper<Prize> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Prize::getStatus, 7);
        if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
            queryWrapper.lambda().ge(Prize::getReleaseTime, startTime).le(Prize::getReleaseTime, endTime);
        }
        Page<Prize> page = new Page<>(request.getPageNum(), request.getPageSize());
        SortUtil.handleWrapperSort(request, queryWrapper, "place", JiebaoConstant.ORDER_DESC, true);
        return this.baseMapper.selectPage(page, queryWrapper);
    }
}
