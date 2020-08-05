package com.jiebao.platfrom.check.dao;

import com.jiebao.platfrom.check.domain.MenusYear;
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
public interface MenusYearMapper extends BaseMapper<MenusYear> {
    @Select("select menus_id from check_menus_year where year_id=#{yearId}")
    List<String> getMenusIdList(String yearId);
}
