package com.jiebao.platfrom.railway.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jiebao.platfrom.common.converter.TimeConverter;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author yf
 */
@Data
@TableName("rail_exchange_user")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Excel("信息互递接收人")
public class ExchangeUser {

    @TableId(value = "id", type = IdType.UUID)
    private String id;

    @ExcelField(value = "和信息互递内容关联ID")
    @ApiModelProperty(value = "和信息互递内容关联ID")
    private String exchangeId;

    @ExcelField(value = "接收信息的用户ID")
    @ApiModelProperty(value = "接收信息的用户ID")
    private String sendUserId;

    @ExcelField(value = "发件时间", writeConverter = TimeConverter.class)
    @ApiModelProperty(value = "发件时间")
    private Date creatTime;


    /**
     * 1收件，2回收 回收站只放收件箱的信息
     */
    @ApiModelProperty(value = "放在该用户的哪个箱：1收件，2回收")
    private String type;

    @ApiModelProperty(value = "阅读状态0、未查看 1、已阅读未回复 2、已阅读已回复",example = "1")
    private Integer isRead;

    @ApiModelProperty(value = "接收时间")
    private Date receiveTime;

    @ApiModelProperty(value = "意见")
    private String opinion;

    @ApiModelProperty(value = "发送给某用户姓名")
    private String sendUserName;

}
