package com.jiebao.platfrom.room.domain;

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
 * @since 2020-07-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("room_message")
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * 内容
     */
    private String content;

    /**
     * 信息创建时间
     */
    private Date date;

    /**
     * 相关会议id
     */
    private String rocordId;

    /**
     * 对应人的id
     */
    private String userId;

    /**
     * 是否已读  0未读  1 已读
     */
    private Integer readIf;

    /**
     * 0  不参会  1 参会
     */
    private Integer status;

    /**
     * 代替参会人员
     */
    private String takeUser;

    private Integer inviteIf;   //  会议邀请   还是会议接触     0  邀请参会信息     1  接触参会邀请

}
