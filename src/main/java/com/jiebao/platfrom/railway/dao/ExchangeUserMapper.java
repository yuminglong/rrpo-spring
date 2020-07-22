package com.jiebao.platfrom.railway.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiebao.platfrom.railway.domain.Exchange;
import com.jiebao.platfrom.railway.domain.ExchangeUser;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;

/**
 * @author yf
 */
public interface ExchangeUserMapper extends BaseMapper<ExchangeUser> {

/**
 *  parent_id =1  实际上创建信息时，信息就已经发送到接收用户的收件箱，但是exchange的status为未发送状态，发布时改下status即可批量发送
 */
    @Insert("INSERT INTO `rail_exchange_user` (exchange_id,send_user_id,parent_id) VALUES (#{exchangeId},#{sendUserId},1)")
    boolean saveByUserId(String exchangeId, String sendUserId);

    @Delete("DELETE FROM rail_exchange_user WHERE exchange_id =#{exchangeId}")
    boolean deleteByExchangeId(String exchangeId);

    @Delete("DELETE FROM rail_exchange_file WHERE exchange_id =#{exchangeId}")
    boolean deleteFileByExchangeId(String exchangeId);
}
