package com.jiebao.platfrom.railway.service.impl;

import com.jiebao.platfrom.railway.dao.PrizeMapper;
import com.jiebao.platfrom.railway.domain.PrizeUser;
import com.jiebao.platfrom.railway.dao.PrizeUserMapper;
import com.jiebao.platfrom.railway.service.PrizeUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 一事一奖推送人意见表 服务实现类
 * </p>
 *
 * @author yf
 * @since 2020-07-24
 */
@Slf4j
@Service("PrizeUserService")
public class PrizeUserServiceImpl extends ServiceImpl<PrizeUserMapper, PrizeUser> implements PrizeUserService {


    @Autowired
    private PrizeUserMapper prizeUserMapper;

    @Override
    public boolean saveByUser(String prizeId, String sendUser) {
        return prizeUserMapper.saveByUser(prizeId,sendUser);
    }

    @Override
    public boolean deleteByPrizeId(String prizeId) {
        return prizeUserMapper.deleteByPrizeId(prizeId);
    }

    @Override
    public boolean updateByPrizeId(String prizeId, String userName,String auditOpinion,String money) {
        return prizeUserMapper.updateByPrizeId(prizeId,userName,auditOpinion,money);
    }


}
