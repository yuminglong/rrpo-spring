package com.jiebao.platfrom.attendance.daomain;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * @since 2020-08-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("attendance_rule")
public class Rule implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(value = "rule_id", type = IdType.UUID)
    private String ruleId;

    /**
     * 考勤规则名字
     */
    private String name;

    /**
     * 上班时间
     */
    private String startDate;

    /**
     * 下班时间
     */
    private String endDate;

    /**
     * 考勤创办时间
     */
    private LocalDateTime creatDate;

    /**
     * 星期一    0上班  1不上班
     */
    private Integer monday;

    /**
     * 周二
     */
    private Integer tuesday;

    /**
     * 周三
     */
    private Integer wednesday;

    /**
     * 周四
     */
    private Integer thursday;

    /**
     * 周五
     */
    private Integer friday;

    /**
     * 周六
     */
    private Integer saturday;

    /**
     * 周日
     */
    private Integer week;
    //是否启用
    private Integer using;

    private Integer maxLate; //最大迟到时间 单位分钟
}
