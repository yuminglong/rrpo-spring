package com.jiebao.platfrom.check.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
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
    private String yearDate;

    private String userId;

    /**
     * 对应考核id
     */
    private String checkId;


    private double num;
}
