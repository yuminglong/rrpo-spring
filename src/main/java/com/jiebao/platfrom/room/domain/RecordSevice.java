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
@TableName("room_record_sevice")
public class RecordSevice implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 会议id
     */
    private String roomRecordId;

    /**
     * 服务id
     */
    private String roomServiceId;


}
