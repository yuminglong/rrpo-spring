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
@TableName("accident_train")
public class Train implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 车务段id
     */
    private String trainId;

    /**
     * 车务段名字
     */
    private String name;


}
