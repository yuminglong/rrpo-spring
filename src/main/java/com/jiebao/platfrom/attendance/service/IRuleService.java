package com.jiebao.platfrom.attendance.service;

import com.jiebao.platfrom.attendance.daomain.Rule;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.domain.QueryRequest;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author qta
 * @since 2020-08-07
 */
public interface IRuleService extends IService<Rule> {
    JiebaoResponse addOrUpdate(Rule rule);

    JiebaoResponse pageList(QueryRequest queryRequest, String name);
}
