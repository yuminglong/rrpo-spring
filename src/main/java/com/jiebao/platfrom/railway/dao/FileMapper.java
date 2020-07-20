package com.jiebao.platfrom.railway.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiebao.platfrom.railway.domain.Files;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author yf
 */
public interface FileMapper extends BaseMapper<Files> {
    //Mapper查询示例一
    //TODO: 可使用注解/xml配置来编写SQL语句

    @Select("SELECT * FROM rail_area")
    List<Files> getFileList();

    /**
     * 删除部门时，其部门下的文件将移入公共文件夹
     *
     * @param parentsId
     */
    void updateByIds(String parentsId);

    /**
     * 根据部门ID查询文件
     *
     * @param parentsId 部门id
     * @return
     */
    @Select("SELECT * FROM `rail_file` r where r.pid =#{parentsId}")
    List<Files> getByParentsId(String parentsId);

    /**
     * 根据userID查询文件信息
     *
     * @param userId userId
     * @return
     */
    @Select("SELECT * FROM `rail_file` r ,rail_file_user u WHERE r.id = u.file_id AND u.user_id =#{userId}")
    List<Files> findByUser(String userId);
}
