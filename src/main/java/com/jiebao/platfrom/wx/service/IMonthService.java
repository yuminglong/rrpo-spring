package com.jiebao.platfrom.wx.service;

import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.domain.QueryRequest;
import com.jiebao.platfrom.wx.domain.Month;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author qta
 * @since 2020-08-22
 */
public interface IMonthService extends IService<Month> {
    JiebaoResponse pageList(QueryRequest queryRequest, String month, Integer look, Integer status);  //月份 分页查询

    JiebaoResponse appear(String monthId, Integer status);  //上报


}
