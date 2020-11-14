package com.jiebao.platfrom.accident.daomain;

import lombok.Data;

@Data
public class compareTable {
    /**
     * bj   代表 比较    up 代表 上期指数
     */
    private String deptName;

    private Double upNumber;
    private Double number;
    private Double bj1;

    private Double upDnxs;
    private Double dnxs;
    private Double bj2;

    private Double upDnTjxs;
    private Double dntjxs;
    private Double bj3;

    private Double upDeathToll;
    private Double deathToll;
    private Double bj4;
}
