package com.jiebao.platfrom.room.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.domain.QueryRequest;
import com.jiebao.platfrom.room.domain.Record;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author qta
 * @since 2020-07-20
 */
public interface IRecordService extends IService<Record> {
     JiebaoResponse addRecord(Record record, List<String> leadListId,List<String> userListId,List<String> fileListId,List<String> serviceListId,List<String> wayListId ,Integer fileState);  //会议的创建
    //  领导id集合  参会人员id集合  附件集合  服务集合  ， 通知方式集合     附件是否能公开  0公开  1非公开


     JiebaoResponse selectByMy(QueryRequest request,String userId,String order);//得到 传入userId  查询 目的人 创建的会议记录   不传 查询当前登陆人  asc dsc

}
