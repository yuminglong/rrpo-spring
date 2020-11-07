package com.jiebao.platfrom.accident.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiebao.platfrom.accident.daomain.Ql;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jiebao.platfrom.common.domain.QueryRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author qta
 * @since 2020-11-07
 */
public interface IQlService extends IService<Ql> {
    IPage<Ql> listPage(QueryRequest queryRequest,String deptName,String policeName,String gwdName);

    boolean importExcel(String deptName,String policeName,String gwdName);

    boolean addOrUpdate(Ql ql);

    boolean delete(String[] idList);

}
