package com.jiebao.platfrom.accident.daomain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;
import java.io.Serializable;

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
 * @since 2020-08-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("accident_case")
public class Case implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 涉铁主键
     */
    @TableId(value = "case_id", type = IdType.UUID)
    private String caseId;

    /**
     * 发生时间
     */
    private LocalDateTime date;

    /**
     * 发生城市
     */
    private String cityCsId;
    @TableField(exist = false)
    private Dept deptCs;
    /**
     * 发生县区
     */
    private String cityQxId;
    @TableField(exist = false)
    private Dept deptQx;
    /**
     * 发生乡镇村
     */
    private String cityXc;

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
    private Dict dictLine;
    /**
     * 地点
     */
    private String address;

    /**
     * 案件性质分类
     */
    private String nature;

    /**
     * 停车时长
     */
    private Double dateLength;

    /**
     * 案件状态
     */
    private String status;

    /**
     * 封闭状态
     */
    private String fbStatus;

    /**
     * 属地管理责任说明
     */
    private String content;

    /**
     * 备注
     */
    private String remark;

    /**
     * 描述
     */
    private String ms;


}
