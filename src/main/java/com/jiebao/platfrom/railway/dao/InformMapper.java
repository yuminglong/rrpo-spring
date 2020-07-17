package com.jiebao.platfrom.railway.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiebao.platfrom.demo.domain.Demo;
import com.jiebao.platfrom.railway.domain.Inform;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author yf
 */
public interface InformMapper extends BaseMapper<Inform> {


    @Select("SELECT * FROM rail_inform")
    List<Inform> getInformList();


    @Insert("INSERT INTO rail_inform_dept (dept_id,inform_id) VALUES (#{deptId},#{informId})")
    boolean setInformDept(String deptId, String informId);

    @Insert("INSERT INTO rail_inform_user (user_id,inform_id) VALUES (#{userId},#{informId})")
    boolean setInformUser(String userId,String informId);
}
