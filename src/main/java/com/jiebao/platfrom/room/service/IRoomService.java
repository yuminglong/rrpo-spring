package com.jiebao.platfrom.room.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jiebao.platfrom.common.domain.QueryRequest;
import com.jiebao.platfrom.room.domain.Room;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author qta
 * @since 2020-07-20
 */
public interface IRoomService extends IService<Room> {

    IPage<Room> list(QueryRequest queryRequest, String name, String address, Integer small, Integer big);  // 姓名模糊查询 地址模糊查询   small会议室容纳最小值  big 会议室最大值
}
