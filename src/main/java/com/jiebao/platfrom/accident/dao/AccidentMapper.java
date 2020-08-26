package com.jiebao.platfrom.accident.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiebao.platfrom.accident.daomain.Accident;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author qta
 * @since 2020-08-04
 */
public interface AccidentMapper extends BaseMapper<Accident> {
    @Select("select * from accident_accident ${ew.customSqlSegment}")
    @Results({
            @Result(property = "deptCs", column = "city_cs_id", one = @One(select = "com.jiebao.platfrom.system.dao.DeptMapper.selectById")),
            @Result(property = "deptQx", column = "city_qx_id", one = @One(select = "com.jiebao.platfrom.system.dao.DeptMapper.selectById")),
            @Result(property = "deptPolice", column = "police_id", one = @One(select = "com.jiebao.platfrom.system.dao.DeptMapper.selectById")),
            @Result(property = "dictXl", column = "line_id", one = @One(select = "com.jiebao.platfrom.system.dao.DictMapper.selectById")),
            @Result(property = "dictCwd", column = "train_id", one = @One(select = "com.jiebao.platfrom.system.dao.DictMapper.selectById")),
            @Result(property = "dictGwd", column = "track_id", one = @One(select = "com.jiebao.platfrom.system.dao.DictMapper.selectById")),
    })
    IPage<Accident> ListPage(Page<Accident> page, @Param("ew") QueryWrapper queryWrapper);


    @Select("select nature from accident_accident group by nature")
    List<String> sgXz();  //事故性质类别

    @Select("select age from accident_accident group by age")
    List<String> age();  //年龄类别

    @Select("select conditions from accident_accident group by conditions")
    List<String> conditions();  //事故情形类别

    @Select("select identity from accident_accident group by identity")
    List<String> identity();  //身份类别

    @Select("select count(*) from accident_accident ${ew.customSqlSegment}")
    Integer count(@Param("ew") QueryWrapper<Accident> ew);  //数量
}
