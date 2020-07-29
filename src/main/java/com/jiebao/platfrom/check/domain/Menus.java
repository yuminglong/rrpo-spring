package com.jiebao.platfrom.check.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Transient;

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
@TableName("check_menus")
public class Menus implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 考核菜单列表
     */
    @TableId(value = "menus_id", type = IdType.UUID)
    private String menusId;

    @ApiModelProperty(value = "考核父类")
    private String parentId;

    /**
     * 考核模块分数
     */
    @ApiModelProperty(value = "最大分值")
    private double grade;

    @ApiModelProperty(value = "最小分值")
    private double minGrade;
    @ApiModelProperty(value = "考核项 最大分值")
    private double maxGrade;
    @TableField(exist = false)
    private List<Menus> childMenus;
    @ApiModelProperty(value = "考核内容")
    private String content;
}
