package com.jiebao.platfrom.room.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.domain.QueryRequest;
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
    JiebaoResponse addUSer(String id, List<String> userId);  //绑定人员

    Boolean deleteByListId( List<String> idList);  //批量删除人员

    IPage<Users> list(QueryRequest queryRequest, String id,Integer leadIf); //  通过会议 id  是否领导查询绑定人员  分页

    List<Users> listByRecordId(String id,Integer leadIf); //  通过会议 id  是否领导查询绑定人员  不分页


}
