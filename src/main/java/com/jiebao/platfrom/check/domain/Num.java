package com.jiebao.platfrom.check.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.jiebao.platfrom.system.domain.Dept;
import com.jiebao.platfrom.system.domain.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author qta
 * @since 2020-07-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("check_num")
public class Num implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 分数总结id
     */
    private String numId;

    /**
     * 存年份
     */
    @ApiModelProperty(value = "年份 后端生成")
    private String yearDate;

    /**
     * 人员id
     */
    @ApiModelProperty(value = "人员id  暂定")
    private String userId;
    @TableField(exist = false)
    private User user;
    /**
     * 年度分数
     */
    @ApiModelProperty(value = "年度分数")
    private Double number;

    /**
     * 所属组织结构或地区
     */
    @ApiModelProperty(value = "所属组织机构")
    private String deptId;
    @TableField(exist = false)
    private Dept dept;

}
