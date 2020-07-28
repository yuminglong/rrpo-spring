package com.jiebao.platfrom.room.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.domain.QueryRequest;
import com.jiebao.platfrom.room.domain.Record;
import com.jiebao.platfrom.room.domain.Room;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author qta
 * @since 2020-07-20
 */
public interface IRecordService extends IService<Record> {
//    JiebaoResponse addRecord(Record record);  //会议的创建

    JiebaoResponse addOrUpdate(Record record);

    JiebaoResponse selectByMy(QueryRequest request, String userId, String order);//得到 传入userId  查询 目的人 创建的会议记录   不传 查询当前登陆人  asc dsc

    JiebaoResponse sendEmail(String recordId, String message, Integer inviteIf, List<String> listUserID);

    JiebaoResponse getRecordByRoomIdOrDateOrUserId(QueryRequest queryRequest, Room roomId, Date date, String userId, String order);//  通过 会议室  时间日期    已经创建人   查询 会议   "asc" "dsc"
}
