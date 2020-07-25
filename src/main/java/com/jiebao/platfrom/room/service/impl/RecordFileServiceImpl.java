package com.jiebao.platfrom.room.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiebao.platfrom.room.dao.RecordFileMapper;
import com.jiebao.platfrom.room.domain.RecordFile;
import com.jiebao.platfrom.room.service.IRecordFileService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
public class RecordFileServiceImpl extends ServiceImpl<RecordFileMapper, RecordFile> implements IRecordFileService {

    @Override
    public Boolean addLead(String id, List<String> LeadListId) {  //绑定 会议 领导
        List<RecordFile> list = new ArrayList<>();  //储存对象
        QueryWrapper<RecordFile> queryWrapper = null;
        for (String fileId : LeadListId
        ) {
            RecordFile recordFile = new RecordFile();
            recordFile.setRoomRecordId(id);
            recordFile.setRoomFileId(fileId);
            queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("room_record_id", id);
            queryWrapper.eq("room_file_id", fileId);
            if (this.baseMapper.selectOne(queryWrapper) == null) { //本对象在数据库不存在执行添加
                list.add(recordFile);
            }
        }
        return saveBatch(list);
    }

    @Override
    public Boolean deleteByListId(List<String> idList) {
        this.baseMapper.deleteBatchIds(idList);
        return null;
    }

    @Override
    public List<RecordFile> list(String id) {
        QueryWrapper<RecordFile> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("room_record_id", id);
        return list(queryWrapper);
    }
}
