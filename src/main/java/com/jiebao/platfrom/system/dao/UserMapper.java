package com.jiebao.platfrom.system.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiebao.platfrom.system.domain.Dept;
import com.jiebao.platfrom.system.domain.User;
import org.apache.ibatis.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.List;

public interface UserMapper extends BaseMapper<User> {


    IPage<User> findUserDetail(Page page, @Param("user") User user);

    @Select("select * from sys_user ${ew.customSqlSegment}")
    @Results({
            @Result(property = "deptName",column = "dept_id",one = @One(select="com.jiebao.platfrom.system.dao.DeptMapper.getByName"))
    })
    IPage<User> queryList(Page<User> page, @Param("ew") QueryWrapper<User> ew);

    /**
     * 获取单个用户详情
     *
     * @param username 用户名
     * @return 用户信息
     */
    User findDetail(String username);

    /**
     * 部门下所有人员
     *
     * @param deptId
     * @return
     */
    @Select(" select * from sys_user r join sys_dept d on r.dept_id = d.dept_id where  r.dept_id = #{deptId}")
    List<User> getUserList(String deptId);

    /**
     * 根据部门查人员
     *
     * @param deptId
     * @return
     */

    @Select("select * from sys_user r  where  r.dept_id = #{deptId}")
    List<User> getByDepts(String deptId);


    @Select("select username from sys_user   ${ew.customSqlSegment}")
    List<String> getUserNameByDepts(@Param("ew") QueryWrapper<User> ew);


    @Select("select user_id from sys_user   ${ew.customSqlSegment}")
    List<String> getUserIdByDepts(@Param("ew") QueryWrapper<User> ew);

    @Select("select dept_id from sys_user r  where  r.username = #{userName}")
    String getDeptID(String userName);

    @Select("UPDATE sys_user SET dept_id = #{deptId} WHERE user_id = #{userId}")
    boolean updateDept(String deptId, String userId);

}