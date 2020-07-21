package com.jiebao.platfrom.room.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.room.dao.RecordMapper;
import com.jiebao.platfrom.room.domain.Record;
import com.jiebao.platfrom.room.service.ILeadService;
import com.jiebao.platfrom.room.service.IRecordService;
import com.jiebao.platfrom.room.service.IUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author qta
 * @since 2020-07-20
 */
@Service
public class RecordServiceImpl extends ServiceImpl<RecordMapper, Record> implements IRecordService {
    @Autowired
    ILeadService leadService;
    @Autowired
    IUsersService usersService;

    @Override
    public JiebaoResponse addRecord(Record record, List<String> leadListId, List<String> userListId, List<String> fileListId, List<String> serviceListId, List<String> wayListId, Integer fileState) {

        return null;
    }
}
