package com.jiebao.platfrom.railway.dao;

import com.jiebao.platfrom.railway.domain.Prize;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * <p>
 * 一事一奖内容表 Mapper 接口
 * </p>
 *
 * @author yf
 * @since 2020-07-24
 */
public interface PrizeMapper extends BaseMapper<Prize> {

    /**
     * 发布，status改为3
     *
     * @param prizeId
     * @return
     */
    @Update("UPDATE  rail_prize set status = 3,release_time = now() where id = #{prizeId}")
    boolean release(String prizeId);

    /**
     * 驳回
     *
     * @param prizeId
     * @return
     */
    @Update("UPDATE rail_prize SET status = 2 WHERE id =#{prizeId}")
    boolean reject(String prizeId);


    @Update("UPDATE rail_prize SET status = 7 WHERE id =#{prizeId}")
    boolean updateStatusForPro(String prizeId);

    @Update("UPDATE rail_prize SET status = 6 WHERE id =#{prizeId}")
    boolean updateStatusForIron(String prizeId);

    @Update("UPDATE rail_prize SET status = 5 WHERE id =#{prizeId}")
    boolean updateStatusForCity(String prizeId);


    @Select("SELECT * FROM rail_prize WHERE `status`=7 AND #{startTime} <= release_time  and release_time <= #{endTime}")
    List<Prize> selectByBriefing(String startTime, String endTime);


    @Select("SELECT max(number) FROM rail_prize")
    Integer findMaxNumber();
}

