package com.jiebao.platfrom.railway.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiebao.platfrom.railway.domain.PrizeOpinion;
import com.jiebao.platfrom.railway.domain.PrizeType;
import org.apache.ibatis.annotations.Select;

/**
 * @author yf
 */
public interface PrizeOpinionMapper extends BaseMapper<PrizeOpinion> {

    /**
     * 判断是否存在 ，以防止重复提交
     * @param rank
     * @param prizeId
     * @return
     */
    @Select("SELECT * FROM `rail_prize_opinion` WHERE rank =#{rank} and prize_id =#{prizeId}")
    boolean selectOpinion(Integer rank,String prizeId);
}
