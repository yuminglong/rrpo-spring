package com.jiebao.platfrom.wx.dao;

import com.jiebao.platfrom.wx.domain.Month;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author qta
 * @since 2020-08-22
 */
public interface MonthMapper extends BaseMapper<Month> {
    @Select("select count(*) from  wx_month where month=#{month} and last_dept=#{deptId}")
    Integer count(String month, String deptId);
}
