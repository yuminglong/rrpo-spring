package com.jiebao.platfrom.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "sys_files")
public class File extends Model<File> implements Serializable {

    private static final long serialVersionUID = 8117574773241604801L;

    @Override
    protected Serializable pkVal() {
        return fileId;
    }

    @TableId(value = "file_id", type = IdType.UUID)
    private String fileId;

    private String oldName;

    private String newName;

    private String fileType;

    private String fileSuffix;

    private String fileUrl;

    private String accessUrl;

    private String refType;

    private String refId;

    private Date time;

    private String userId;

    private Boolean status;

}