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
@TableName("room_lead")
public class Lead implements Serializable {  //绑定领导

    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * 会议id
     */
    private String mettingId;

    /**
     * 领导id
     */
    private String userLeadId;

    /**
     * 是否参会  0 参会  1不参会
     */
    private Integer state;


}
