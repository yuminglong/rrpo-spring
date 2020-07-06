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
 * 通讯录
 *
 * @author yf
 */
@Data
@TableName("rail_address")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Excel("通讯录")
public class Address {

    @TableId(value = "id",type = IdType.UUID)
    private String id;

    private String parentsId;

    @ExcelField(value = "名称")
    private String userName;


    @ExcelField(value = "电话")
    private  String phone;

    @ExcelField(value = "手机")
    private String telPhone;

    @ExcelField(value = "微信")
    private String weiXin;

    @ExcelField(value = "邮箱")
    private String email;

    /**
     * 序号
     */
    private Double numbers;

    @TableField(exist = false)
   // @JsonProperty("title")
    @ExcelField(value = "部门名")
    private String deptName;

    @ExcelField(value = "创建时间", writeConverter = TimeConverter.class)
    private Date creatTime;

    private String userId;

    /**
     * 状态1正常
     */
    private Integer status;

}
