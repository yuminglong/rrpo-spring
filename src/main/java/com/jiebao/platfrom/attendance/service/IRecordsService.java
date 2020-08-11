package com.jiebao.platfrom.attendance.service;

import com.jiebao.platfrom.attendance.daomain.Record;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.domain.QueryRequest;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author qta
 * @since 2020-08-07
 */
public interface IRecordsService extends IService<Record> {
    JiebaoResponse punch(Integer ifLate);   //打卡接口

    JiebaoResponse list(QueryRequest queryRequest, String deptId, String name, String date);  //分页查询 打卡记录t

    JiebaoResponse selectIfLate();//此接口返回是上班打卡 还是  下班打卡    0 上班 1 下班


}
