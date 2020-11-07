package com.jiebao.platfrom.accident.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
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
@TableName("accident_ql")
public class Ql implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("ID")
    private Integer id;

    private String bm;

    private String dzs;

    private String xsq;

    private String xz;

    private String zl;

    private String ga;

    private String pcs;

    private String jwq;

    private String gwd;

    private String gls;

    private String fw;

    private String cd;

    private String jtsgqk;

    private String yy;

    private String kzqk;

    private String tp;

    private String delflag;


}
