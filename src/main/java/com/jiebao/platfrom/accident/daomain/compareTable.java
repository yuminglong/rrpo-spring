package com.jiebao.platfrom.accident.daomain;

import lombok.Data;

@Data
public class compareTable {
    /**
     * bj   代表 比较    up 代表 上期指数
     */
    private String deptName;

    private Integer upNumber;
    private Integer number;
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

    public compareTable() {

    }

    public compareTable(String deptName, Integer upNumber, Integer number, Double bj1, Double upDnxs, Double dnxs, Double bj2, Double upDnTjxs, Double dntjxs, Double bj3, Double upDeathToll, Double deathToll, Double bj4) {
        this.deptName = deptName;
        this.upNumber = upNumber;
        this.number = number;
        this.bj1 = bj1;
        this.upDnxs = upDnxs;
        this.dnxs = dnxs;
        this.bj2 = bj2;
        this.upDnTjxs = upDnTjxs;
        this.dntjxs = dntjxs;
        this.bj3 = bj3;
        this.upDeathToll = upDeathToll;
        this.deathToll = deathToll;
        this.bj4 = bj4;
    }

}
