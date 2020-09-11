package com.jiebao.platfrom.accident.daomain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

import com.jiebao.platfrom.system.domain.Dept;
import com.jiebao.platfrom.system.domain.Dict;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author qta
 * @since 2020-08-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("accident_accident")
public class Accident implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 事故记录id
     */
    @TableId(value = "accident_id", type = IdType.UUID)
    private String accidentId;

    /**
     * 事故发生时间
     */
    @ApiModelProperty(value = "事故发生时间", example = "2020/04/03 12:15:12")
    private String date;

    /**
     * 发生城市
     */
    @ApiModelProperty(value = "发生城市", example = "哈哈")
    private String cityCsId;



    @TableField(exist = false)
    private Dept deptCs;

    /**
     * 发生区县
     */
    @ApiModelProperty(value = "发生区县", example = "哈哈")
    private String cityQxId;
    @TableField(exist = false)
    private Dept deptQx;
    /**
     * 派出所
     */
    @ApiModelProperty(value = "派出所", example = "哈哈")
    private String policeId;

//    @TableField(exist = false)
//    private Dept deptPolice;
    /**
     * 线路
     */
    @ApiModelProperty(value = "线路 字典", example = "哈哈")
    private String lineId;
    @TableField(exist = false)
    private Dict dictXl;
    /**
     * 地点
     */
    @ApiModelProperty(value = "地点", example = "哈哈")
    private String address;

    /**
     * b
     * 车务段
     */
    @ApiModelProperty(value = "车务段", example = "哈哈")
    private String trainId;
    @TableField(exist = false)
    private Dict dictCwd;
    /**
     * 公务段
     */
    @ApiModelProperty(value = "公务段", example = "哈哈")
    private String trackId;

    @TableField(exist = false)
    private Dict dictGwd;
    /**
     * 事故性质
     */
    @ApiModelProperty(value = "事故性质", example = "哈哈")
    private String nature;

    /**
     * 站内性质
     */
    @ApiModelProperty(value = "站内性质", example = "哈哈")
    private String instationSection;

    /**
     * 护路模式
     */
    @ApiModelProperty(value = "护路模式", example = "哈哈")
    private String road;

    /**
     * 年龄段
     */
    @ApiModelProperty(value = "年龄段", example = "哈哈")
    private String age;

    /**
     * 封闭程度
     */
    @ApiModelProperty(value = "封闭程度", example = "哈哈")
    private String closed;

    /**
     * 居住地
     */
    @ApiModelProperty(value = "居住地", example = "哈哈")
    private String jzd;

    /**
     * 离铁路远近
     */
    @ApiModelProperty(value = "离铁路远近", example = "哈哈")
    private String distance;

    /**
     * 性别
     */
    @ApiModelProperty(value = "性别", example = "哈哈")
    private String sex;

    /**
     * 身份判断
     */
    @ApiModelProperty(value = "身份判断", example = "哈哈")
    private String identity;

    /**
     * 事故情形
     */
    @ApiModelProperty(value = "事故情形", example = "哈哈")
    private String conditions;

    /**
     * 电脑分析系数
     */
    @ApiModelProperty(value = "电脑分析系数", example = "哈哈")
    private String dnxs;

    /**
     * 电脑调节系数
     */
    @ApiModelProperty(value = "电脑调节系数", example = "哈哈")
    private String dntjxs;

    /**
     * 公安分析系数
     */
    @ApiModelProperty(value = "公安分析系数", example = "哈哈")
    private String gaxs;

    /**
     * 属地管理责任死亡人数
     */
    @ApiModelProperty(value = "属地管理责任死亡人数", example = "哈哈")
    private String deathToll;

    private Integer statu;

}
