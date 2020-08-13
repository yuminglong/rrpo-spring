package com.jiebao.platfrom.railway.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiebao.platfrom.common.domain.QueryRequest;
import com.jiebao.platfrom.railway.domain.Exchange;
import com.jiebao.platfrom.railway.domain.Prize;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 一事一奖内容表 服务类
 * </p>
 *
 * @author yf
 * @since 2020-07-24
 */
public interface PrizeService extends IService<Prize> {
    /**
     * 查询发件箱
     * @param request
     * @param prize
     * @param startTime
     * @param endTime
     * @return
     */
    IPage<Prize> getPrizeList(QueryRequest request, Prize prize , String startTime, String endTime);


    IPage<Prize> getPrizeInboxList(QueryRequest request, Prize prize , String startTime, String endTime);

}
