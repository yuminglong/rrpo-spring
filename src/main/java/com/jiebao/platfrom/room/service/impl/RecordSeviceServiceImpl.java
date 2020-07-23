package com.jiebao.platfrom.room.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiebao.platfrom.common.domain.QueryRequest;
import com.jiebao.platfrom.room.dao.RecordSeviceMapper;
import com.jiebao.platfrom.room.domain.RecordSevice;
import com.jiebao.platfrom.room.domain.Users;
import com.jiebao.platfrom.room.service.IRecordSeviceService;
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
public class RecordSeviceServiceImpl extends ServiceImpl<RecordSeviceMapper, RecordSevice> implements IRecordSeviceService {
    @Override
    public Boolean addLead(String id, List<String> LeadListId) {  //绑定 会议 人员
        List<RecordSevice> list = new ArrayList<>();  //储存对象
        QueryWrapper<RecordSevice> queryWrapper = null;
        for (String sid : LeadListId) {  //服务项

            RecordSevice recordSevice = new RecordSevice();
            recordSevice.setRoomRecordId(id);
            recordSevice.setRoomServiceId(sid);
            queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("room_record_id", id);
            queryWrapper.eq("room_file_id", sid);
            if (this.baseMapper.selectOne(queryWrapper) == null) { //本对象在数据库不存在执行添加
                list.add(recordSevice);
            }
        }
        return saveBatch(list);
    }


    @Override
    public Boolean deleteByListId(String id, List<String> idList) {
        QueryWrapper<RecordSevice> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("room_file_id", idList);
        queryWrapper.eq("room_record_id", id);
        this.baseMapper.delete(queryWrapper);
        return null;
    }


    @Override
    public IPage<RecordSevice> list(QueryRequest queryRequest,String id) {
        QueryWrapper<RecordSevice> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("room_record_id", id);
        Page<RecordSevice> page = new Page<>(queryRequest.getPageNum(),queryRequest.getPageSize());
        return this.baseMapper.selectPage(page,queryWrapper);
    }
}
