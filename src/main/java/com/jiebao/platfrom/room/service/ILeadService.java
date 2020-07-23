package com.jiebao.platfrom.room.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jiebao.platfrom.common.domain.QueryRequest;
import com.jiebao.platfrom.room.domain.Lead;
import com.jiebao.platfrom.room.domain.Record;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author qta
 * @since 2020-07-20
 */
public interface ILeadService extends IService<Lead> {

    Boolean addLead(String id, List<String> LeadListId);

    Boolean deleteByListId(String id, List<String> idList);

    IPage<Lead> list(QueryRequest request, String id);
}
