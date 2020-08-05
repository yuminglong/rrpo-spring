package com.jiebao.platfrom.check.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiebao.platfrom.check.domain.Num;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiebao.platfrom.system.domain.User;
import org.apache.ibatis.annotations.*;

import java.sql.Wrapper;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author qta
 * @since 2020-07-28
 */
public interface NumMapper extends BaseMapper<Num> {
    @Select("select * from check_num ${ew.customSqlSegment}")
    @Results({
            @Result(property = "dept", column = "dept_id", one = @One(select = "com.jiebao.platfrom.system.dao.DeptMapper.selectById"))
    })
    IPage<Num> pageList(Page<Num> page, @Param("ew") QueryWrapper wrapper);

    @Select("select * from check_num where num_id=#{numId}")
    @Results({
            @Result(property = "dept", column = "dept_id", one = @One(select = "com.jiebao.platfrom.system.dao.DeptMapper.selectById"))
    })
    Num selectByNumId(String numId);

}
