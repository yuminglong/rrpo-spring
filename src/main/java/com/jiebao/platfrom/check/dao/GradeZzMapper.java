package com.jiebao.platfrom.check.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jiebao.platfrom.check.domain.GradeZz;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author qta
 * @since 2020-08-11
 */
public interface GradeZzMapper extends BaseMapper<GradeZz> {
    @Select("select zz_id from check_grade_zz where type=#{type} and grade_id=#{gradeId}")
    List<String> getZzId(Integer type, String gradeId);

    @Update("update check_grade_zz set status=#{status} ${customSqlSegment}")
    int updateByGrade(Integer status, QueryWrapper<GradeZz> ew);
}
