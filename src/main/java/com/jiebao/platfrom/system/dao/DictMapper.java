package com.jiebao.platfrom.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiebao.platfrom.system.domain.Dict;
import org.apache.ibatis.annotations.Select;

public interface DictMapper extends BaseMapper<Dict> {

    @Select("SELECT * FROM `sys_dict_new` d where d.field_name = #{deptName}  ")
    Dict getByNewNames(String deptName);

    @Select("SELECT dict_id FROM `sys_dict_new` d where d.field_name = #{dictName}  ")
    String getIdByName(String dictName);
}