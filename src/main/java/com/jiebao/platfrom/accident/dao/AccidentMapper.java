package com.jiebao.platfrom.accident.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiebao.platfrom.accident.daomain.ANumber;
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
            @Result(property = "sexDict", column = "sex", one = @One(select = "com.jiebao.platfrom.system.dao.DictMapper.selectById")),
            @Result(property = "dictNature", column = "nature", one = @One(select = "com.jiebao.platfrom.system.dao.DeptMapper.selectById")),
            @Result(property = "dictInstationSection", column = "instation_section", one = @One(select = "com.jiebao.platfrom.system.dao.DictMapper.selectById")),
            @Result(property = "dictRoad", column = "road", one = @One(select = "com.jiebao.platfrom.system.dao.DictMapper.selectById")),
            @Result(property = "dictAge", column = "age", one = @One(select = "com.jiebao.platfrom.system.dao.DictMapper.selectById")),
            @Result(property = "dictClosed", column = "closed", one = @One(select = "com.jiebao.platfrom.system.dao.DictMapper.selectById")),
    })
    IPage<Accident> ListPage(Page<Accident> page, @Param("ew") QueryWrapper<Accident> queryWrapper);


    @Select("select ${name} as subscript,count(1) as number from accident_accident ${ew.customSqlSegment}")
    List<ANumber> listAcc(@Param("ew") QueryWrapper<Accident> queryWrapper, @Param("name") String name);
}
