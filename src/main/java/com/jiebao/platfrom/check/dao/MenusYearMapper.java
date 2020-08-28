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
    @Select("select menus_id from check_menus_year where year_id=#{yearId} and parent_id=#{parentId}")
    List<String> getMenusIdList(String yearId, String parentId);


    @Select("select count(*) from check_menus_year where year_id=#{yearId} and parent_id=(select standard_id from check_menus where content='基础工作')")
    Integer jcNumber(String yearId);

    @Select("select count(*) from check_menus_year where year_id=#{yearId} and parent_id=(select standard_id from check_menus where content='工作效果')")
    Integer xgNumber(String yearId);

    @Select("select count(*) from check_menus_year where year_id=#{yearId}")
    Integer countNumber(String yearId);

    @Select("select 1 from check_menus_year where content=#{content} limit 1")
    Integer exSit(String content);
}
