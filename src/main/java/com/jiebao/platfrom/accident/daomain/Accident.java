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
    @TableId(value = "accident_id",type = IdType.UUID)
    private String accidentId;

    /**
     * 事故发生时间
     */
    private Date date;

    /**
     * 发生城市
     */
    private String cityCsId;

    @TableField(exist = false)
    private Dept deptCs;

    /**
     * 发生区县
     */
    private String cityQxId;
    @TableField(exist = false)
    private Dept deptQx;
    /**
     * 派出所
     */
    private String policeId;
    @TableField(exist = false)
    private Dept deptPolice;
    /**
     * 线路
     */
    private String lineId;
    @TableField(exist = false)
    private Dict dictXl;
    /**
     * 地点
     */
    private String address;

    /**
     * 车务段
     */
    private String trainId;
    @TableField(exist = false)
    private Dict dictCwd;
    /**
     * 公务段
     */
    private String trackId;
    @TableField(exist = false)
    private Dict dictGwd;
    /**
     * 事故性质
     */
    private String nature;

    /**
     * 站内性质
     */
    private String instationSection;

    /**
     * 护路模式
     */
    private String road;

    /**
     * 年龄段
     */
    private String age;

    /**
     * 封闭程度
     */
    private String closed;

    /**
     * 居住地
     */
    private String jzd;

    /**
     * 离铁路远近
     */
    private String distance;

    /**
     * 性别
     */
    private String sex;

    /**
     * 身份判断
     */
    private String identity;

    /**
     * 事故情形
     */
    private String condition;

    /**
     * 电脑分析系数
     */
    private String dnxs;

    /**
     * 电脑调节系数
     */
    private String dntjxs;

    /**
     * 公安分析系数
     */
    private String gaxs;

    /**
     * 属地管理责任死亡人数
     */
    private String deathToll;


}
