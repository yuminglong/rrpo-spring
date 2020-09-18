package com.jiebao.platfrom.wx.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiebao.platfrom.wx.domain.Qun;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiebao.platfrom.wx.domain.QunExcel;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author qta
 * @since 2020-08-20
 */
public interface QunMapper extends BaseMapper<Qun> {
    @Select("select *,(select count(*) from wx_user u where u.qun_id=wx_id) as number from wx_qun ${ew.customSqlSegment}")
    @Results({
            @Result(property = "deptJc", column = "cj_dept_id", one = @One(select = "com.jiebao.platfrom.system.dao.DeptMapper.selectById")),
            @Result(property = "deptSh", column = "sh_dept_id", one = @One(select = "com.jiebao.platfrom.system.dao.DeptMapper.selectById"))
    })
    IPage<Qun> list(Page<Qun> page, QueryWrapper<Qun> ew);


    @Select("select 1 from wx_qun where cj_dept_id=#{deptId} limit 1")
    Integer judge(String deptId);


    @Select("select wx_name as wxName,(select dept_name from sys_dept where dept_id=cj_dept_id) as villageDeptName,#{cityDeptName}  as cityDeptName, (select count(*) from wx_user u where u.qun_id=wx_id) as number from wx_qun ${ew.customSqlSegment}")
    List<QunExcel> listExcel(@Param("cityDeptName") String cityDeptName, @Param("ew") QueryWrapper<Qun> qunQueryWrapper);  //以市州去查

}