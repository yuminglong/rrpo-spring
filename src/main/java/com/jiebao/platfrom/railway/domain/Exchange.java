package com.jiebao.platfrom.railway.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jiebao.platfrom.common.converter.TimeConverter;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
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
@TableName("rail_exchange")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Excel("信息互递内容表")
public class Exchange {

    @TableId(value = "id", type = IdType.UUID)
    private String id;

    @ExcelField(value = "标题")
    private String title;

    @ExcelField(value = "内容")
    private String content;

    @ExcelField(value = "创建时间", writeConverter = TimeConverter.class)
    private Date createTime;

    @ExcelField(value = "创建人")
    private String createUser;

    @ExcelField(value = "状态：1未发送 2撤销的 3已发布")
    @ApiModelProperty(value ="状态：1未发送 2撤销的 3已发布")
    private Integer status;

}
