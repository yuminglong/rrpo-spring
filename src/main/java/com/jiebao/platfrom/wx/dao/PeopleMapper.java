package com.jiebao.platfrom.wx.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiebao.platfrom.wx.domain.People;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author qta
 * @since 2020-10-16
 */
public interface PeopleMapper extends BaseMapper<People> {

    @Select("select hl_id as hlId,(select dept_name from sys_dept where dept_id=w.shi) as shiName, (select dept_name from sys_dept where dept_id=w.qu_xian) " +
            "  as  quXian,(select dept_name from sys_dept where dept_id=w.xiang)   as xiang,proper ,line,name,sex,age,face,address,id_card as idCard,lu_duan as" +
            " luDuan,phone,is_wx isWx,is_qun as isQun,police from wx_people w ${ew.customSqlSegment}")
    IPage<People> listPage(Page<People> page, @Param("ew") QueryWrapper<People> queryWrapper);

    @Update("update wx_people set status=#{status}")
    Integer lock(Integer status);


}
