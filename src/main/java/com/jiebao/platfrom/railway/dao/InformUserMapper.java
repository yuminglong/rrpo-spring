package com.jiebao.platfrom.railway.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiebao.platfrom.railway.domain.ExchangeUser;
import com.jiebao.platfrom.railway.domain.InformUser;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;

/**
 * @author yf
 */
public interface InformUserMapper extends BaseMapper<InformUser> {

    /**
     * 新增
     *
     * @param sendUserId
     * @param informId
     * @return
     */
    @Insert("INSERT INTO `rail_inform_user` (inform_id,send_user_id,type) VALUES (#{informId},#{sendUserId},1)")
    boolean sendUser(String sendUserId, String informId);
}
