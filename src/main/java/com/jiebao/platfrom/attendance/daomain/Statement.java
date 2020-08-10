package com.jiebao.platfrom.attendance.daomain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author qta
 * @since 2020-08-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("attendance_statement")
public class Statement implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "statement_id", type = IdType.UUID)
    private String statementId;

    /**
     * 人员对象
     */
    private String userId;

    /**
     * 人员名字
     */
    private String userName;

    private String deptId;

    private String deptName;

    /**
     * 迟到次数
     */
    private Integer late;

    /**
     * 早退
     */
    private Integer leave;

    /**
     * 缺卡
     */
    private Integer dummy;

    /**
     * 矿工
     */
    private Integer miner;

    /**
     * 时间日期
     */
    private Date StartDate;


    private Date endDate;

    private String dateDay;
}
