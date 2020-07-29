package com.jiebao.platfrom.railway.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiebao.platfrom.common.authentication.JWTUtil;
import com.jiebao.platfrom.common.domain.JiebaoConstant;
import com.jiebao.platfrom.common.domain.QueryRequest;
import com.jiebao.platfrom.common.utils.SortUtil;
import com.jiebao.platfrom.railway.domain.Exchange;
import com.jiebao.platfrom.railway.domain.Prize;
import com.jiebao.platfrom.railway.dao.PrizeMapper;
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


    @Override
    public IPage<Prize> getPrizeList(QueryRequest request, Prize prize, String startTime, String endTime) {
        QueryWrapper<Prize> queryWrapper = new QueryWrapper<>();
        String username = JWTUtil.getUsername((String) SecurityUtils.getSubject().getPrincipal());
        queryWrapper.lambda().ne(Prize::getStatus, 4);
        if (username !=null){
            queryWrapper.lambda().eq(Prize::getCreatUser,username);
        }
        if (StringUtils.isNotBlank(prize.getTitle())) {
            queryWrapper.lambda().eq(Prize::getTitle, prize.getTitle());
        }
        if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
            queryWrapper.lambda().ge(Prize::getCreatTime, startTime).le(Prize::getCreatTime, endTime);
        }
        Page<Prize> page = new Page<>(request.getPageNum(),request.getPageSize());
        SortUtil.handleWrapperSort(request,queryWrapper,"creatTime", JiebaoConstant.ORDER_DESC,true);
        return this.baseMapper.selectPage(page,queryWrapper);
    }
}
