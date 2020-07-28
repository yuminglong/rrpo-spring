package com.jiebao.platfrom.check.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
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
 * @since 2020-07-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("check_num")
public class Num implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 分数总结id
     */
    private String numId;

    /**
     * 存年份
     */
    private String yearDate;

    /**
     * 人员id
     */
    private String userId;

    /**
     * 年度分数
     */
    private Double number;

    /**
     * 所属组织结构或地区
     */
    private String deptId;


}
