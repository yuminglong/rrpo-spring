package com.jiebao.platfrom.room.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.domain.QueryRequest;
import com.jiebao.platfrom.room.dao.RecordMapper;
import com.jiebao.platfrom.room.domain.Record;
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
    ILeadService leadService;
    @Autowired
    IUsersService usersService;
    @Autowired
    IServiceService serviceService;
    @Autowired
    IWayService wayService;
    @Autowired
    IFilesService filesService;

    @Override
    public JiebaoResponse addRecord(Record record, List<String> leadListId, List<String> userListId, List<String> fileListId, List<String> serviceListId, List<String> wayListId, Integer fileState) {
        JiebaoResponse jiebaoResponse = new JiebaoResponse();
        if (record.getStartDate() == null || record.getEndDate() == null) {
            return jiebaoResponse.message("请选择开会时间区间");
        }
        if (record.getId() == null) {   //添加操作
            boolean save = save(record);

        } else {

        }

        return null;
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

    private void add() {

    }

    private void updates() {

    }

    @Override
    public boolean saveOrUpdate(Record entity) {
        if (entity.getId() == null) {
            entity.setCreatDate(new Date());
        }
        return super.saveOrUpdate(entity);
    }


}
