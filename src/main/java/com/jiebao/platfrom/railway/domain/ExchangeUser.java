package com.jiebao.platfrom.railway.domain;


import com.baomidou.mybatisplus.annotation.IdType;
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
 *
 *
 * @author yf
 */
@Data
@TableName("rail_exchange_user")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Excel("信息互递")
public class ExchangeUser {

    @TableId(value = "id", type = IdType.UUID)
    private String id;

    @ExcelField(value = "和信息互递内容关联ID")
    private String exchangeId;

    @ExcelField(value = "和用户关联ID")
    private String userId;

    @ExcelField(value = "创建时间", writeConverter = TimeConverter.class)
    private Date creatTime;


    @ExcelField(value = "信息互递信息状态")
    private Integer status;

}
