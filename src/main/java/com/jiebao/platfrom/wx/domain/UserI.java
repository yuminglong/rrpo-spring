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
@TableName("wx_user")
public class UserI implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(value = "wx_user_id",type = IdType.UUID)
    private String wxUserId;

    /**
     * 名字
     */
    private String name;

    /**
     * 身份证
     */
    private String idCar;

    private String phone;

    /**
     * 身份
     */
    private String sf;

    /**
     * 创建时间
     */
    private Date date;


}
