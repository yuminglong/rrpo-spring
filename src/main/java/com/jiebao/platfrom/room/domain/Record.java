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
@TableName("room_record")
public class Record implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.UUID)
    private String id;
    /**
     * 会议名称 主题
     */
    private String name;

    /**
     *  会议室绑定
     */
    private String roomId;

    /**
     * 会议附件绑定
     */
    private String fileId;

    /**
     * 会议绑定服务  瓜子 饮料 大鸡腿
     */
    private String serviceId;


}
