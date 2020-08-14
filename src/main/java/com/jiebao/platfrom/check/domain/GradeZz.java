package com.jiebao.platfrom.check.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author qta
 * @since 2020-08-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("check_grade_zz")
public class GradeZz implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 扣分佐证id
     */
    @TableId(value = "grade_zz_id", type = IdType.UUID)
    private String gradeZzId;

    private String gradeId;

    private Integer type;

    /**
     * 佐证id
     */
    private String zzId;

    private Integer status;     //0  0 正常  1不正常


}
