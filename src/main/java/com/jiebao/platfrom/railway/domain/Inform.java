package com.jiebao.platfrom.railway.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jiebao.platfrom.common.converter.TimeConverter;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 通知公告
 *
 * @author yf
 */
@Data
@TableName("rail_inform")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Excel("通知公告")
public class Inform {

    @TableId(value = "id", type = IdType.UUID)
    private String id;

    @TableField(exist = false)
    private String key;

    @ExcelField(value = "标题")
    private String title;

    @ExcelField(value = "内容")
    private String content;

    @ExcelField(value = "创建时间", writeConverter = TimeConverter.class)
    private Date createTime;

    /**
     * 来源
     */
    private String period;

    @ExcelField(value = "创建人")
    private String createUser;

    private Integer status;

    /**
     * 上级ID
     */
    private String parentId;

    private Integer type;
}
