package com.jiebao.platfrom.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiebao.platfrom.system.domain.Dept;
import com.jiebao.platfrom.system.domain.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.List;

public interface UserMapper extends BaseMapper<User> {


    IPage<User> findUserDetail(Page page, @Param("user") User user);

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


    @Select("select username from sys_user r  where  r.dept_id = #{deptId}")
    List<String> getUserIdByDepts(String deptId);

    @Select("select dept_id from sys_user r  where  r.username = #{userName}")
    String getUser(String userName);

}