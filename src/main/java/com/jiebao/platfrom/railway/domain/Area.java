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
 * 地区表
 *
 * @author yf
 */
@Data
@TableName("rail_area")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Excel("地区表")
public class Area {

    @TableId(value = "id", type = IdType.UUID)
    private String id;

    @ApiModelProperty(value = "父级ID，如：天心区的parentId就是长沙市id")
    private String parentId;

    @ExcelField(value = "地区名")
    @ApiModelProperty(value = "地区名")
    private String areaName;

    /**
     * 地区阶级1、市级 2、区级 3、街道或镇级
     */
    @ApiModelProperty(value = "地区阶级1、市级 2、区级 3、街道或镇级")
    private Integer rank;

    /**
     * 邮编
     */
    @ExcelField(value = "邮编")
    @ApiModelProperty(value = "邮编")
    private String areaCode;

    /**
     * 地区名
     */

    @ApiModelProperty(value = "修改人")
    private String updateUser;

    @ApiModelProperty(value = "创建时间")
    @ExcelField(value = "创建时间", writeConverter = TimeConverter.class)
    private Date creatTime;


}
