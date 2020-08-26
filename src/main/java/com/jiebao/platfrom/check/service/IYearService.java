package com.jiebao.platfrom.check.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jiebao.platfrom.check.domain.Year;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.domain.QueryRequest;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author qta
 * @since 2020-08-05
 */
public interface IYearService extends IService<Year> {
    JiebaoResponse addOrUpdate(Year year);  //年份考核规则

    JiebaoResponse pageList(QueryRequest queryRequest, String yearDate);//年份查询

    JiebaoResponse yearStringList();//已存入的年份集合

    List<Year> list(QueryWrapper<Year> queryWrapper);

}
