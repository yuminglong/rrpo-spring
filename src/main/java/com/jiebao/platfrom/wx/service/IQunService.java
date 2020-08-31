package com.jiebao.platfrom.wx.service;

import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.domain.QueryRequest;
import com.jiebao.platfrom.wx.domain.Qun;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author qta
 * @since 2020-08-20
 */
public interface IQunService extends IService<Qun> {
    JiebaoResponse pageList(QueryRequest queryRequest, String name, String userName);

    JiebaoResponse updateStatus(String qunId);

    JiebaoResponse up(String qunId);
}
