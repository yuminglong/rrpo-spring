package com.jiebao.platfrom.room.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.domain.QueryRequest;
import com.jiebao.platfrom.room.dao.UsersMapper;
import com.jiebao.platfrom.room.domain.Record;
import com.jiebao.platfrom.room.domain.Room;
import com.jiebao.platfrom.room.domain.Users;
import com.jiebao.platfrom.room.service.IRecordService;
import com.jiebao.platfrom.room.service.IRoomService;
import com.jiebao.platfrom.room.service.IUsersService;
import org.springframework.beans.factory.annotation.Autowired;
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
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements IUsersService {
    @Autowired
    IRecordService recordService;
    @Autowired
    IRoomService roomService;

    @Override
    public JiebaoResponse addUSer(String id, List<String> userListId) {  //绑定 会议 人员
        int size = listByRecordId(id, null).size();  //会议现阶段邀请人数
        Record record = recordService.getById(id);
        Room room = roomService.getById(record.getRoomId());  //得到对应的会议室
        if (size + userListId.size() > room.getPeopleNum()) {  //当 绑定 已经已存在人数 大于最大容纳人数时
            return new JiebaoResponse().message("超出最大容纳人数" + (size + userListId.size() - room.getPeopleNum()));
        }
        List<Users> list = new ArrayList<>();  //储存对象
        QueryWrapper<Users> queryWrapper = null;
        for (String userId : userListId
        ) {
            Users Users = new Users();
            Users.setMeetingId(id);
            Users.setUserId(userId);
            Users.setStatus(null);
            queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("meeting_id", id);
            queryWrapper.eq("user_id", userId);
            if (this.baseMapper.selectOne(queryWrapper) == null) { //本对象在数据库不存在执行添加
                list.add(Users);
            }
        }
        return new JiebaoResponse().message(saveBatch(list) ? "添加成员成功" : "添加成员失败");
    }

    @Override
    public Boolean deleteByListId(List<String> idList) {
        this.baseMapper.deleteBatchIds(idList);
        return null;
    }


    @Override
    public IPage<Users> list(QueryRequest queryRequest, String id, Integer leadIf) {
        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        if (id != null) {
            queryWrapper.eq("metting_id", id);
        }
        if (leadIf != null) {
            queryWrapper.eq("lead_if", id);
        }
        Page<Users> page = new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize());
        return this.baseMapper.selectPage(page, queryWrapper);
    }

    public List<Users> listByRecordId(String id, Integer leadIf) {
        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        if (id != null) {
            queryWrapper.eq("metting_id", id);
        }
        if (leadIf != null) {
            queryWrapper.eq("lead_if", id);
        }
        return list(queryWrapper);
    }
}
