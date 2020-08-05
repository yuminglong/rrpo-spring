package com.jiebao.platfrom.check.dao;

import com.jiebao.platfrom.check.domain.Menus;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import javax.annotation.Resource;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author qta
 * @since 2020-07-28
 */
@Resource
public interface MenusMapper extends BaseMapper<Menus> {
    @Select("select menus_id from check_menus where content=#{name}")
    String getMenusIdByName(String name);//通过name   也就是 content内容获得对应主键id
}
