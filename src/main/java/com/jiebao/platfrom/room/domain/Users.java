package com.jiebao.platfrom.room.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author qta
 * @since 2020-07-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("room_user")
public class Users implements Serializable {   //会议绑定人员

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * 会议id
     */
    private String meetingId;

    /**
     * 人员id
     */
    private String userId;


    /**
     * 是否参加  回执 是否参加  0  不参加  1参加
     */
    private Integer status;

    /**
     * 代替参会
     */
    private String takeUserId;

    private Integer leadIf;   //是否是领导  0  是 1 不是

}
