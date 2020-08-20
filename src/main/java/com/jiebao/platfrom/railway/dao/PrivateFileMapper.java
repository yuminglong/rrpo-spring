package com.jiebao.platfrom.railway.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiebao.platfrom.railway.domain.PrivateFile;
import com.jiebao.platfrom.railway.domain.PublicFile;
import com.jiebao.platfrom.system.domain.File;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author yf
 */
public interface PrivateFileMapper extends BaseMapper<PrivateFile> {

    /**
     * 根据publicId查对应的公共文件
     * @param refId
     * @return
     */
    @Select("SELECT * FROM `sys_files` WHERE ref_id = #{refId} and ref_type = 5 ORDER BY time DESC")
    List<File> selectFiles(String refId);
}
