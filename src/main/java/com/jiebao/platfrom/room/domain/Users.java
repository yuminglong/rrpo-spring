package com.jiebao.platfrom.room.domain;

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
public class Users implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 会议id  
     */
    private String meetingId;

    /**
     * 人员id
     */
    private String userId;


}
