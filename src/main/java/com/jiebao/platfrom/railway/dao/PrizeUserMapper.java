package com.jiebao.platfrom.railway.dao;

import com.jiebao.platfrom.railway.domain.PrizeUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 * 一事一奖推送人意见表 Mapper 接口
 * </p>
 *
 * @author yf
 * @since 2020-07-24
 */
public interface PrizeUserMapper extends BaseMapper<PrizeUser> {
    /**
     * 发送人保存到数据库
     *
     * @param prizeId    一事一奖内容ID
     * @param sendUser 接收人
     * @return
     */
    @Insert("INSERT INTO `rail_prize_user` (prize_id,send_user) VALUES (#{prizeId},#{sendUser})")
    boolean saveByUser(String prizeId, String sendUser);

    /**
     * 根据prizeId删除相应的接收人
     * @param prizeId
     * @return
     */
    @Delete("DELETE FROM rail_prize_user WHERE prize_id =#{prizeId}")
    boolean deleteByPrizeId(String prizeId);

    /**
     * 发表审核意见
     * @param prizeId
     * @param userName
     * @param auditOpinion
     * @return
     */
    @Update("UPDATE `rail_prize_user` set audit_opinion = #{auditOpinion} ,money = #{money} where prize_id =#{prizeId} and send_user= #{userName}")
    boolean updateByPrizeId(String prizeId, String userName,String auditOpinion,String money);
}
