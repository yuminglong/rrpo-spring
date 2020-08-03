package com.jiebao.platfrom.railway.domain;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 一事一奖内容表
 * </p>
 *
 * @author yf
 * @since 2020-07-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("rail_prize")
@Accessors(chain = true)
public class Prize {

    @TableId(value = "id", type = IdType.UUID)
    private String id;

    private String title;

    /**
     * 事迹简要描述
     */

    private String content;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date creatTime;

    @ApiModelProperty(value = "创建人")
    private String creatUser;

    /**
     * 单位编号
     */
    @ApiModelProperty(value = "单位编号")
    private String unitNumber;

    private String name;

    private Integer age;

    private String sex;

    /**
     * 身份
     */
    @ApiModelProperty(value = "身份")
    private String identity;

    /**
     * 发布时间
     */
    @ApiModelProperty(value = "发布时间")
    private Date releaseTime;

    /**
     * 发布地点
     */
    @ApiModelProperty(value = "发布地点")
    private String releasePlace;

    /**
     * 由谁发布：本人、他人
     */
    @ApiModelProperty(value = "由谁发布：本人、他人")
    private String releaseWho;

    /**
     * 编号
     */
    @ApiModelProperty(value = "编号")
    private String number;

    /**
     * 状态3，已发送   4已删除
     */
    @ApiModelProperty(value = "状态：1、未发送 2、撤回 3、已发送  4、已删除")
    private String status;
}