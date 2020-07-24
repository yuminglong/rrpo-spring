package com.jiebao.platfrom.railway.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jiebao.platfrom.railway.domain.Exchange;
import com.jiebao.platfrom.railway.domain.ExchangeUser;

/**
 * @author yf
 */
public interface ExchangeUserService extends IService<ExchangeUser> {

    /**
     *  信息互递选定要发送的的用户存入数据库
     * @param exchangeId 信息互递id
     * @param sendUserId  用户ID
     * @return
     */
        boolean saveByUserId(String exchangeId,String sendUserId);

    /**
     * 删除掉选定的用户
     * @param exchangeId
     * @return
     */
    boolean deleteByExchangeId(String exchangeId);
}
