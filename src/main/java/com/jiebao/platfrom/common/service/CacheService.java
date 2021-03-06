package com.jiebao.platfrom.common.service;

import com.jiebao.platfrom.system.domain.*;

import java.util.List;
import java.util.Map;

public interface CacheService {

    /**
     * 测试 Redis是否连接成功
     */
    void testConnect() throws Exception;

    /**
     * 从缓存中获取用户
     *
     * @param username 用户名
     * @return User
     */
    User getUser(String username) throws Exception;

    /**
     * 从缓存中获取用户角色
     *
     * @param username 用户名
     * @return 角色集
     */
    List<Role> getRoles(String username) throws Exception;

    /**
     * 从缓存中获取用户权限
     *
     * @param username 用户名
     * @return 权限集
     */
    List<Menu> getPermissions(String username) throws Exception;

    /**
     * 从缓存中获取用户个性化配置
     *
     * @param userId 用户 ID
     * @return 个性化配置
     */
    UserConfig getUserConfig(String userId) throws Exception;

    /**
     * 缓存用户信息，只有当用户信息是查询出来的，完整的，才应该调用这个方法
     * 否则需要调用下面这个重载方法
     *
     * @param user 用户信息
     */
    void saveUser(User user) throws Exception;

    /**
     * 缓存用户信息
     *
     * @param username 用户名
     */
    void saveUser(String username) throws Exception;

    /**
     * 缓存用户角色信息
     *
     * @param username 用户名
     */
    void saveRoles(String username) throws Exception;

    /**
     * 缓存用户权限信息
     *
     * @param username 用户名
     */
    void savePermissions(String username) throws Exception;

    /**
     * 缓存用户个性化配置
     *
     * @param userId 用户 ID
     */
    void saveUserConfigs(String userId) throws Exception;

    /**
     * 删除用户信息
     *
     * @param username 用户名
     */
    void deleteUser(String username) throws Exception;

    /**
     * 删除用户角色信息
     *
     * @param username 用户名
     */
    void deleteRoles(String username) throws Exception;

    /**
     * 删除用户权限信息
     *
     * @param username 用户名
     */
    void deletePermissions(String username) throws Exception;

    /**
     * 删除用户个性化配置
     *
     * @param userId 用户 ID
     */
    void deleteUserConfigs(String userId) throws Exception;

    /**
     * 缓存组织机构信息
     *
     * @throws Exception
     */
    void saveDept() throws Exception;

    /**
     * 缓存信息互递传阅人子集部门
     */
     void saveExchangeDept() throws Exception;

    /**
     * 读取信息互递传阅人子集部门
     * @return
     * @throws Exception
     */
     List<Dept> getExchangeDept(String deptId) throws Exception;

    /**
     * 缓存信息互递查询部门的传阅人员
     */
    void saveExchangeUser() throws Exception;

    /**
     * 读取信息互递查询部门的传阅人员
     * @param deptId
     * @return
     * @throws Exception
     */
    List<User> getExchangeUser(String deptId) throws Exception;







    /**
     * 读取组织机构缓存信息
     *
     * @return
     * @throws Exception
     */
    Map<String, Object> getDept() throws Exception;




    /**
     * 缓存十四个市州等的作为主节点的所有子节点用户信息
     */
    void saveAllChildrenDept() throws Exception;

    /**
     * 读取十四个市州等的作为主节点的所有子节点用户信息
     *
     * @param deptId
     * @return
     * @throws Exception
     */
    List<Dept> getAllChildrenDept(String deptId) throws Exception;



}
