package com.jiebao.platfrom.wx.domain;

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
 * @since 2020-09-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wx_notice")
public class Notice implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 公告序列
     */
    @TableId(value = "notice_id", type = IdType.UUID)
    private String noticeId;

    /**
     * 内容
     */
    private String content;

    /**
     * 时间
     */
    private Date date;


}
