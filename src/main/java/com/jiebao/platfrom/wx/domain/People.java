package com.jiebao.platfrom.wx.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

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
 * @since 2020-10-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wx_people")
public class People implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 护路id
     */
    @TableId(value = "hl_id", type = IdType.UUID)
    private String hlId;

    @ApiModelProperty(value = "添加对象专属id")
    @TableField(exist = false)
    private String deptId;
    /**
     * 市名称
     */
    private String shi;

    @TableField(exist = false)
    private String shiName;

    /**
     * 区县名称
     */

    private String quXian;
    @TableField(exist = false)
    private String quXianName;
    /**
     * 乡镇
     */
    private String xiang;
    @TableField(exist = false)
    private String xiangName;

    /**
     * 队员属性
     */
    private String proper;

    /**
     * 线路
     */
    private String line;

    /**
     * 名字
     */
    private String name;

    /**
     * sex
     */
    private String sex;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 政治面貌
     */
    private String face;

    /**
     * 家庭地址
     */
    private String address;

    /**
     * 身份证号码
     */
    private String idCard;

    /**
     * 路段——责任
     */
    private String luDuan;

    /**
     * 电话号码
     */
    private String phone;

    /**
     * 是否使用微信
     */
    private String isWx;

    /**
     * 是否加入群
     */
    private String isQun;

    /**
     * 派出所
     */
    private String police;

    /**
     * 0 正常   1此信息是新增的  2 此信息是要删除的
     */
    private Integer status;

}
