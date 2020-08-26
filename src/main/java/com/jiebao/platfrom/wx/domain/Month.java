package com.jiebao.platfrom.wx.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.jiebao.platfrom.system.domain.Dept;
import com.jiebao.platfrom.system.domain.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 *
 * </p>
 *
 * @author qta
 * @since 2020-08-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wx_month")
public class Month implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "wx_mon_id",type = IdType.UUID)
    private String wxMonthId;

    /**
     * 月份
     */
    private String month;

    /**
     * 内容
     */
    private String content;

    /**
     * 来源于什么群
     */
    private String qunId;
    @TableField(exist = false)
    private Qun qun;
    /**
     * 起始地区
     */
    private String jcDeptId;
    @TableField(exist = false)
    private Dept deptJc;

    /**
     * 递交到那一层
     */
    private String shDeptId;
    @TableField(exist = false)
    private Dept deptSh;

    //是否进入年核
    private Integer isCheck;
    //
    private String userId;
    @TableField(exist = false)
    private User user;

    private Date date;
    //最后审核部门 不用管
    private String lastDeptId;
    @TableField(exist = false)
    private Dept lastDept;
}
