package com.jiebao.platfrom.check.domain;

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
 * @since 2020-08-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("check_menus_year")
public class MenusYear implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 年度  菜单 挂钩
     */
    private String menusYearId;

    /**
     * 菜单
     */
    private String menusId;

    /**
     * 年份表id
     */
    private String yearId;


}
