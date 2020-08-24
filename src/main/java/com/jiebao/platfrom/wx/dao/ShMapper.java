package com.jiebao.platfrom.wx.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiebao.platfrom.wx.domain.Qun;
import com.jiebao.platfrom.wx.domain.Sh;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
 * @since 2020-08-20
 */
public interface ShMapper extends BaseMapper<Sh> {
    @Select("select * from wx_sh ${ew.customSqlSegment}")
    @Results({
            @Result(property = "dept", column = "dept_id", one = @One(select = "com.jiebao.platfrom.system.dao.DeptMapper.selectById"))
//            @Result(property = "shDeptId", column = "sh_dept_id", one = @One(select = "com.jiebao.platfrom.system.dao.DeptMapper..selectById"))
    })
    List<Sh> list(Page<Sh> page, QueryWrapper<Sh> ew);

}
