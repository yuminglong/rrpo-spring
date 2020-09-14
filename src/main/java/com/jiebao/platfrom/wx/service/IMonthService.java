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

    JiebaoResponse koran(String MonthId, Integer status);//省级触发  审批意见可入否   1 可以 0不可入

    void monthDocx(String month);//微信推荐汇总导出

    JiebaoResponse monthDocxText(QueryRequest queryRequest, String month);  //展示效果

}
