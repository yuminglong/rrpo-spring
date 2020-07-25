package com.jiebao.platfrom.room.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiebao.platfrom.room.dao.WayMapper;
import com.jiebao.platfrom.room.domain.Users;
import com.jiebao.platfrom.room.domain.Way;
import com.jiebao.platfrom.room.service.IWayService;
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
public class WayServiceImpl extends ServiceImpl<WayMapper, Way> implements IWayService {


    @Override
    public Boolean addLead(String id, List<Integer> LeadListId) {  //绑定 会议 消息发送方式   提醒方式  0  邮件  1 短信 2 及时信息
        List<Way> list = new ArrayList<>();  //储存对象
        QueryWrapper<Way> queryWrapper = null;
        for (Integer wid : LeadListId
        ) {
            Way way = new Way();
            way.setRoomReordId(id);
            way.setWay(wid);
            queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("room_reord_id", id);
            queryWrapper.eq("way", wid);
            if (this.baseMapper.selectOne(queryWrapper) == null) { //本对象在数据库不存在执行添加
                list.add(way);
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
    public List<Way> list(String id) {
        QueryWrapper<Way> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("room_reord_id", id);
        return list(queryWrapper);
    }
}
