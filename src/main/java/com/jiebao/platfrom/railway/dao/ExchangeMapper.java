package com.jiebao.platfrom.railway.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiebao.platfrom.railway.domain.Exchange;
import com.jiebao.platfrom.railway.domain.ExchangeFile;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author yf
 */
public interface ExchangeMapper extends BaseMapper<Exchange> {

    @Update("UPDATE  rail_exchange  r set  r.`status` = 4 WHERE r.id =#{exchangeId}")
    boolean updateStatus(String exchangeId);


    @Update("UPDATE  rail_exchange  r set  r.`status` = 3 WHERE r.id =#{exchangeId}")
    boolean release(String exchangeId);

}
