package com.jiebao.platfrom.attendance.daomain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

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
@TableName("attendance_record")
public class Record implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "record_id", type = IdType.UUID)
    private String recordId;

    /**
     * 人员对象
     */
    private String userId;

    /**
     * 打开人姓名
     */
    private String name;

    /**
     * 上班打卡时间
     */
    private Date startDate;

    private Date endDate;



    private String deptId;
    private String dept;

    private String dateDay;
}
