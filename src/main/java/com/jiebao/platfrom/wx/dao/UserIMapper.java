package com.jiebao.platfrom.wx.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiebao.platfrom.wx.domain.UserI;
import org.apache.ibatis.annotations.Param;
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
public interface UserIMapper extends BaseMapper<UserI> {
    @Select("select count(*) from wx_user where qun_id=#{wxId}")
    Integer countByWxId(@Param("wxId") String wxId);


    @Select("select wx_id from wx_qun ${ew.customSqlSegment} ")
    List<String> listWxId(@Param("ew") QueryWrapper queryWrapper);//得到所求部门下所有的  群
}