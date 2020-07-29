package com.jiebao.platfrom.railway.dao;

import com.jiebao.platfrom.railway.domain.Prize;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;

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
     * @param prizeId
     * @return
     */
    @Update("UPDATE  rail_prize set status = '3' where id = #{prizeId}")
    boolean release(String prizeId);

}
