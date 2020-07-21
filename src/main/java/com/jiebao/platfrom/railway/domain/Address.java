package com.jiebao.platfrom.railway.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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

    /**
     * 部门ID
     */
    @ApiModelProperty(value ="和部门关联ID")
    private String parentsId;

    @ExcelField(value = "名称")
    @ApiModelProperty(value ="名称")
    private String userName;


    @ExcelField(value = "电话")
    @ApiModelProperty(value ="电话")
    private  String phone;

    @ExcelField(value = "手机")
    @ApiModelProperty(value ="手机")
    private String telPhone;

    @ExcelField(value = "微信")
    private String weiXin;

    @ExcelField(value = "邮箱")
    private String email;

    /**
     * 序号
     */
    @ApiModelProperty(value ="排序号")
    private Double numbers;

    @TableField(exist = false)
   // @JsonProperty("title")
    @ExcelField(value = "部门名")
    private String deptName;

   // @ExcelField(value = "创建时间", writeConverter = TimeConverter.class)
   @ApiModelProperty(value ="创建时间")
    private Date creatTime;

    @ApiModelProperty(value ="和user关联ID")
    private String userId;

    /**
     * 状态1正常
     */
    @ApiModelProperty(value ="状态",example = "1")
    private Integer status;

    @ApiModelProperty(value ="和地区关联ID")
    private  String areaId;

}
