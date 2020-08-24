package com.jiebao.platfrom.wx.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiebao.platfrom.wx.domain.Month;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiebao.platfrom.wx.domain.Qun;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

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

    @Select("select * from wx_qun ${ew.customSqlSegment}")
    @Results({
            @Result(property = "deptJc", column = "cj_dept_id", one = @One(select = "com.jiebao.platfrom.system.dao.DeptMapper.selectById")),
            @Result(property = "deptSh", column = "sh_dept_id", one = @One(select = "com.jiebao.platfrom.system.dao.DeptMapper.selectById"))
    })
    List<Qun> list(Page<Qun> page, QueryWrapper<Qun> ew);
}
