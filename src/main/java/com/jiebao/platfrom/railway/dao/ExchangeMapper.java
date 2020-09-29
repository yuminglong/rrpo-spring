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


    @Update("UPDATE  rail_exchange  r set  r.`status` = 3 ,r.release_time = now() WHERE r.id =#{exchangeId}")
    boolean release(String exchangeId);


    @Update("UPDATE  rail_exchange  r set  r.release_time = now() WHERE r.id =#{exchangeId}")
    boolean releaseSave(String exchangeId);

    @Select("SELECT * FROM `rail_exchange` where  title= #{title}   and info_seq =#{infoSeq}")
    List<Exchange> selectId(String infoSeq,String title);
}
