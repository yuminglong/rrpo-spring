package com.jiebao.platfrom.check.domain;

import lombok.Data;

import java.util.List;

@Data
public class YearZu {
    private String name;
    private String id; //分组类型所属id
    private List<MenusYear> list;
}
