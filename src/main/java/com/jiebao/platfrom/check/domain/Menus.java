package com.jiebao.platfrom.check.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Transient;

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
@TableName("check_menus")
public class Menus implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 考核菜单列表
     */
    @TableId(value = "menus_id", type = IdType.UUID)
    private String menusId;

    /**
     * 考核名字
     */
    private String name;

    private String parentId;

    /**
     * 考核模块分数
     */
    private double grade;


    private double minGrade;

    private double maxGrade;
    @TableField(exist = false)
    private List<Menus> childMenus;

    private String content;
}
