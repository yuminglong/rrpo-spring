package com.jiebao.platfrom.room.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.domain.QueryRequest;
import com.jiebao.platfrom.room.dao.RecordMapper;
import com.jiebao.platfrom.room.domain.Record;
import com.jiebao.platfrom.room.domain.Users;
import com.jiebao.platfrom.room.service.*;
import com.jiebao.platfrom.system.domain.User;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
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
    IUsersService usersService;
    @Autowired
    IServiceService serviceService;
    @Autowired
    IWayService wayService;
    @Autowired
    IFilesService filesService;
    @Autowired
    IMessageService messageService;

    @Override
    public JiebaoResponse addRecord(Record record) {
        JiebaoResponse jiebaoResponse = new JiebaoResponse();
        if (record.getStartDate() == null || record.getEndDate() == null) {
            return jiebaoResponse.message("请选择开会时间区间");
        }
        return new JiebaoResponse().message(save(record) ? "创建成功" : "创建会议失败");
    }

    @Override
    public JiebaoResponse selectByMy(QueryRequest request, String userId, String order) {
        if (userId == null) {  //查询当前登陆人
            User user = (User) SecurityUtils.getSubject().getPrincipal();
            userId = user.getUserId();
        }
        QueryWrapper<Record> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("create_user", userId);
        if (order.equals("asc")) {
            queryWrapper.orderByAsc("creat_date");
        } else {
            queryWrapper.orderByDesc("creat_date");
        }
        Page<Record> page = new Page<>(request.getPageNum(), request.getPageSize());
        return new JiebaoResponse().data(this.baseMapper.selectPage(page, queryWrapper));
    }

    @Override
    public JiebaoResponse sendEmail(String recordId, String message, Integer inviteIf, List<String> listUserID) {
        if (listUserID == null) {  //如果没有传入  执行 全部人员
            QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("meeting_id", recordId);  //对应会议
            List<Users> list = usersService.list(queryWrapper);//得到本次会议  相关人员
            for (Users u : list  //收集id
            ) {
                listUserID.add(u.getId());
            }
        }
        return messageService.addList(recordId, listUserID, message, inviteIf);
    }


    @Override
    public boolean saveOrUpdate(Record entity) {
        if (entity.getId() == null) {
            entity.setCreatDate(new Date());
        }
        return super.saveOrUpdate(entity);
    }


}
