package com.jiebao.platfrom.wx.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

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
 * @since 2020-08-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wx_sh")
public class Sh implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(value = "wx_sh_id", type = IdType.UUID)
    private String wxShId;

    private String deptId;

    private String wxQunId;

    /**
     * 0通过  1不通过
     */
    private Integer status;

    private Date shDate;

    private String massage;

    private String userId;
}
