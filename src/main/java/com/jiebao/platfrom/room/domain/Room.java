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
@TableName("room_room")
public class Room implements Serializable {  //会议室

    private static final long serialVersionUID = 1L;
@TableId(value = "id",type = IdType.UUID)
    private String id;
    /**
     * 会议室名字
     */
    private String name;

    /**
     * 会议室 地址
     */
    private String address;

    /**
     * 会议室容纳人员
     */
    private Integer peopleNum;


}
