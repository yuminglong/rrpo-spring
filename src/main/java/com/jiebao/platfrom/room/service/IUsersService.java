package com.jiebao.platfrom.room.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.domain.QueryRequest;
import com.jiebao.platfrom.room.domain.Lead;
import com.jiebao.platfrom.room.domain.Users;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author qta
 * @since 2020-07-20
 */
public interface IUsersService extends IService<Users> {
    JiebaoResponse addLead(String id, List<String> LeadListId);

    Boolean deleteByListId( List<String> idList);

    IPage<Users> list(QueryRequest queryRequest, String id);

    List<Users> listByRecordId(String id);
}
