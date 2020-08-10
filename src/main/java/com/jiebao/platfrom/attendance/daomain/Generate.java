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
@TableName("attendance_generate")
public class Generate implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 报表统计每日自动生成id
     */
    @TableId(value = "generate_id", type = IdType.ID_WORKER)
    private String generateId;

    /**
     * 具体时间
     */
    private Date date;


}
