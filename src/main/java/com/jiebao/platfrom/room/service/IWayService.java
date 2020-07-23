package com.jiebao.platfrom.room.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jiebao.platfrom.room.domain.Users;
import com.jiebao.platfrom.room.domain.Way;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author qta
 * @since 2020-07-20
 */
public interface IWayService extends IService<Way> {
    Boolean addLead(String id, List<Integer> LeadListId);

    Boolean deleteByListId(String id, List<String> idList);

    List<Way> list(String id);
}
