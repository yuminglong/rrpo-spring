package com.jiebao.platfrom.room.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jiebao.platfrom.room.domain.RecordFile;
import com.jiebao.platfrom.room.domain.Users;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author qta
 * @since 2020-07-20
 */
public interface IRecordFileService extends IService<RecordFile> {
    Boolean addLead(String id, List<String> LeadListId);

    Boolean deleteByListId(String id, List<String> idList);

    List<RecordFile> list(String id);
}
