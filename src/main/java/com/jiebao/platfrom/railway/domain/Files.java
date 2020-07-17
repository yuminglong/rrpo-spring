package com.jiebao.platfrom.railway.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 文件表
 *
 * @author yf
 */
@Data
@TableName("rail_file")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Files {

    @TableId(value = "id",type = IdType.UUID)
    private String id;

    @ApiModelProperty(value ="名称")
    private String title;

    private Date createTime;

    private String url;

    /**
     * 来源
     */
    @ApiModelProperty(value ="来源")
    private String period;

    @ApiModelProperty(value ="创建人(后台直接获取)")
    private String createUser;


    @ApiModelProperty(value ="和部门关联id")
    private  String pid;

    @ApiModelProperty(value ="和个人用户关联id")
    private  String uid;


    @ApiModelProperty(value ="类型")
    private Integer type;



}
