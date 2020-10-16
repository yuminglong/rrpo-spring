package com.jiebao.platfrom.wx.service;

import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.domain.QueryRequest;
import com.jiebao.platfrom.wx.domain.People;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author qta
 * @since 2020-10-16
 */
public interface IPeopleService extends IService<People> {

    JiebaoResponse listPage(QueryRequest queryRequest, String DeptId, Integer rank);  //分页查询

    JiebaoResponse saveOrUpdateChile(People people);

    JiebaoResponse lock(Integer status);  //上锁 解锁

    JiebaoResponse checkLock();
}
