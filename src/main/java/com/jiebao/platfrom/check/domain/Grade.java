package com.jiebao.platfrom.check.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import java.io.Serializable;

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
@TableName("check_grade")
public class Grade implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(value = "grade_id",type = IdType.UUID)
    private String gradeId;

    /**
     * 具体年限
     */
    @ApiModelProperty(value = "年份 后端生成不用传",example = "哈哈")
    private String yearDate;
    @ApiModelProperty(value = "人员id  暂定",example = "哈哈")
    private String userId;

    /**
     * 对应考核id
     */
    @ApiModelProperty(value = "对应的考核项id",example = "哈哈")
    private String checkId;

    @ApiModelProperty(value = "分数",example = "2.0")
    private double num;
}
