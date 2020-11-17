package com.jiebao.platfrom.railway.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiebao.platfrom.railway.domain.Briefing;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

/**
 * @author yf
 */
public interface BriefingMapper extends BaseMapper<Briefing> {

    @Update("UPDATE  rail_briefing  r set  r.`status` = 4 WHERE r.id =#{briefingId}")
    boolean updateStatus(String briefingId);


    @Update("UPDATE  rail_briefing  r set  r.`status` = 3 ,r.release_time = now() WHERE r.id =#{briefingId}")
    boolean release(String briefingId);


    @Update("UPDATE  rail_briefing  r set  r.release_time = now() WHERE r.id =#{briefingId}")
    boolean releaseSave(String briefingId);

    List<Map<String,Object>> countCityById(String briefingId);
}
