package com.jiebao.platfrom.wx.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

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

    /**
     * 起始地区
     */
    private String dept;

    /**
     * 递交到那一层
     */
    private String shDept;
  //是否进入年核
    private Integer isCheck;
  //
    private String userId;
    @DateTimeFormat(style = "yyyy-MM-dd HH:mm:ss")
    private Date date;
   //最后审核部门 不用管
    private String lastDept;
}
