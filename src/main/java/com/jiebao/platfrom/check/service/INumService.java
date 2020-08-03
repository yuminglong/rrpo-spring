package com.jiebao.platfrom.check.service;

import com.jiebao.platfrom.check.domain.Num;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.domain.QueryRequest;

import java.time.Year;
import java.util.Date;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author qta
 * @since 2020-07-28
 */
public interface INumService extends IService<Num> {
    JiebaoResponse pageList(QueryRequest queryRequest, String userName, String deptId, String dateYear); //参数  人员 部门  时间年份 获得对应数据列表
}
