package com.jiebao.platfrom.check.dao;

import com.jiebao.platfrom.check.domain.Year;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author qta
 * @since 2020-08-05
 */
public interface YearMapper extends BaseMapper<Year> {
    @Select("select year_date from check_year order by  year_date desc")
    List<String> yearStringList();

}
