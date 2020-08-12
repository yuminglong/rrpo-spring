package com.jiebao.platfrom.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiebao.platfrom.system.domain.File;
import org.apache.ibatis.annotations.Update;

public interface FileMapper extends BaseMapper<File> {

    @Update("UPDATE sys_files SET ref_id = #{exchangeId} WHERE file_id =#{fileId}")
    boolean updateByFileId(String fileId,String exchangeId);

    @Update("UPDATE sys_files SET ref_id = #{informId} WHERE file_id =#{fileId}")
    boolean updateInformByFileId(String fileId,String informId);
}
