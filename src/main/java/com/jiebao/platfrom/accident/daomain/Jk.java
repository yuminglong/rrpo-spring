package com.jiebao.platfrom.accident.daomain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * @since 2020-11-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("accident_jk")
public class Jk implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID",type = IdType.UUID)
    private Integer id;

    @TableField("PK")
    private String pk;

    private String gac;

    private String dzs;

    private String xsq;

    private String xzjd;

    private String xlmc;

    private String tllc;

    private String azfw;

    private String tljl;

    private String dlmc;

    private String dllc;

    private String azbw;

    private Integer sl;

    private String jkqlx;

    private Integer jkfw;

    private String jksjzl;

    private String jkysgn;

    private Integer jkgd;

    private Integer blts;

    private String lwdw;

    private String jsdw;

    private String azsjn;

    private String azsj;

    private String gldw;

    private String sydw;

    private String pcs;

    private String sdpcs;

    private String bz;

    private String tp;

    private String delflag;

    private String username;

    private String datalock;


}
