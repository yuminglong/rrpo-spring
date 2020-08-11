package com.jiebao.platfrom.check.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
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

    @ApiModelProperty(value = "考核父类", example = "哈哈")
    private String parentId;

    /**
     * 考核模块分数
     */
    @ApiModelProperty(value = "最大分值", example = "23")
    private double grade;

    @ApiModelProperty(value = "最小分值", example = "23")
    private double minGrade;
    @ApiModelProperty(value = "考核项 最大分值", example = "23")
    private double maxGrade;
    @ApiModelProperty(value = "考核内容", example = "哈哈")
    private String content;
    @ApiModelProperty(value = "创建时间", example = "2020/04/03 12:15:12")
    private Date date;
    @TableField(exist = false)
    private List<Menus> childMenus;

    @TableField(exist = false)
    private double number;

    private Integer accessory;//是否可传附件 0  不可传   1 可传
}
