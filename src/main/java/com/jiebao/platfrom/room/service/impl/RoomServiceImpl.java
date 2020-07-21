package com.jiebao.platfrom.room.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiebao.platfrom.common.domain.QueryRequest;
import com.jiebao.platfrom.room.dao.RoomMapper;
import com.jiebao.platfrom.room.domain.Room;
import com.jiebao.platfrom.room.service.IRoomService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author qta
 * @since 2020-07-20
 */
@Service
public class RoomServiceImpl extends ServiceImpl<RoomMapper, Room> implements IRoomService {


    @Override
    public IPage<Room> list(QueryRequest queryRequest, String name, String address, Integer small, Integer big) {
        QueryWrapper<Room> queryWrapper = new QueryWrapper<>();
        if (name != null) {
            queryWrapper.like("name", name);
        }
        if (address != null) {
            queryWrapper.like("address", address);
        }
        if (small != null) {
            queryWrapper.ge("people_num", small);
        }
        if (big != null) {
            queryWrapper.le("people_num", big);
        }
        Page<Room> page = new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize());
        return this.baseMapper.selectPage(page, queryWrapper);
    }
}
