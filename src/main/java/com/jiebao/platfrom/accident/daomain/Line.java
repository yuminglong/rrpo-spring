package com.jiebao.platfrom.accident.daomain;

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
 * @since 2020-08-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("accident_line")
public class Line implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 线路id
     */
    private String lineId;

    /**
     * 线路名
     */
    private String name;


}
