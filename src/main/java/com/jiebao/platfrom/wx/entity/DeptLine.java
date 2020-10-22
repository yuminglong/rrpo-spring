package com.jiebao.platfrom.wx.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author qta
 * @since 2020-10-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("wx_dept_line")
public class DeptLine implements Serializable {

    private static final long serialVersionUID = 1L;

    private String deptLineId;

    private String deptId;

    private String lineName;


}
