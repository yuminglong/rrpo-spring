package com.jiebao.platfrom.railway.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiebao.platfrom.railway.dao.ExchangeMapper;
import com.jiebao.platfrom.railway.dao.ExchangeUserMapper;
import com.jiebao.platfrom.railway.domain.Exchange;
import com.jiebao.platfrom.railway.domain.ExchangeUser;
import com.jiebao.platfrom.railway.service.ExchangeService;
import com.jiebao.platfrom.railway.service.ExchangeUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service("ExchangeUserService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ExchangeUserServiceImpl extends ServiceImpl<ExchangeUserMapper, ExchangeUser> implements ExchangeUserService {

    @Autowired
    ExchangeUserMapper exchangeUserMapper;

    @Override
    public boolean saveByUserId(String exchangeId, String sendUserId) {
        return exchangeUserMapper.saveByUserId(exchangeId, sendUserId);
    }

    @Override
    public boolean deleteByExchangeId(String exchangeId) {
        boolean result = exchangeUserMapper.deleteByExchangeId(exchangeId) && exchangeUserMapper.deleteFileByExchangeId(exchangeId);
        return result;
    }

    @Override
    public boolean removeBySendUserId(String sendUserId,String exchangeId){
        return exchangeUserMapper.removeBySendUserId(sendUserId,exchangeId);
    }
}
