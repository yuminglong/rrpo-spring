package com.jiebao.platfrom.railway.service;

import com.jiebao.platfrom.railway.domain.PrizeUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 一事一奖推送人意见表 服务类
 * </p>
 *
 * @author yf
 * @since 2020-07-24
 */
public interface PrizeUserService extends IService<PrizeUser> {

    /**
     * 发送人保存到数据库
     *
     * @param prizeId    一事一奖内容ID
     * @param sendUserId 接收人
     * @return
     */
    boolean saveByUser(String prizeId, String sendUser);

    /**
     * 根据prizeId删除相应的接收人
     * @param prizeId
     * @return
     */
    boolean deleteByPrizeId(String prizeId);


    boolean updateByPrizeId(String prizeId,String userName,String auditOpinion,String money);

}
