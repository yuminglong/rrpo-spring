package com.jiebao.platfrom.accident.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jiebao.platfrom.accident.daomain.Accident;
import com.jiebao.platfrom.accident.daomain.Lwswfx;
import com.jiebao.platfrom.accident.service.ILwswfxService;
import com.jiebao.platfrom.system.dao.DeptMapper;
import com.jiebao.platfrom.system.dao.DictMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author qta
 * @since 2020-11-15
 */
@RestController

@RequestMapping("/accident/lwswfx")
@Api(tags = "accident-事故分析")
public class LwswfxController {
    @Autowired
    ILwswfxService lwswfxService;
    @Autowired
    DeptMapper deptMapper;

    @Autowired
    DictMapper dictMapper;

    @GetMapping("list")
    @ApiOperation("事故分析")
    public void list() {
        List<String> shiList = new ArrayList<>();
        List<String> xianList = new ArrayList<>();
        List<String> suoList = new ArrayList<>();
        List<String> cheList = new ArrayList<>();
        List<String> gongList = new ArrayList<>();
        QueryWrapper<Lwswfx> queryWrapper = new QueryWrapper<>();
        queryWrapper.groupBy("fsxsdq");
        for (Lwswfx lwswfx : lwswfxService.list(queryWrapper)) {
            //    System.out.println(lwswfx);
//            if(lwswfx.getFsdsz()!=null)
//            if (deptMapper.getByNewName(lwswfx.getFsdsz()) == null) {
//                shiList.add(lwswfx.getFsdsz());
//            }
            try {
                if (lwswfx.getFsxsdq() != null)
                    if (deptMapper.getByNewName(lwswfx.getFsxsdq()) == null) {
                        xianList.add(lwswfx.getFsxsdq());
                    }
            } catch (MyBatisSystemException e) {
                shiList.add(lwswfx.getFsdsz());
                xianList.add(lwswfx.getFsxsdq());
            }

//            if(lwswfx.getPcs()!=null)
//            if (deptMapper.getByNewName(lwswfx.getPcs()) == null) {
//               suoList.add(lwswfx.getPcs());
//            }
//            if(lwswfx.getCwd()!=null)
//            if (deptMapper.getByNewName(lwswfx.getCwd()) == null) {
//                cheList.add(lwswfx.getCwd());
//            }
//            if(lwswfx.getGwd()!=null)
//            if (deptMapper.getByNewName(lwswfx.getGwd()) == null) {
//                gongList.add(lwswfx.getGwd());
//            }
//
//            if(lwswfx.getXl()!=null)
//                if (dictMapper.getByNewNames(lwswfx.getXl()) == null) {
//                    gongList.add(lwswfx.getXl());
//                }
        }

        for (String o : shiList) {
            System.out.println(o);
        }
        System.out.println("-----------");
        System.out.println("--------------");
        for (String o : xianList) {
            System.out.println(o);
        }
        System.out.println("-----------");
        System.out.println("--------------");
        for (String o : suoList) {
            System.out.println(o);
        }
        System.out.println("-----------");
        System.out.println("--------------");
        for (String o : cheList) {
            System.out.println(o);
        }
        System.out.println("-----------");
        System.out.println("--------------");
        for (String o : gongList) {
            System.out.println(o);
        }
        System.out.println("-----------");
        System.out.println("--------------");
    }

    @GetMapping("addList")
    @ApiOperation("导入数据")
    public void ad() {
        List<Accident> list = new ArrayList<>();
        for (Lwswfx lwswfx : lwswfxService.list()) {
            if (lwswfx.getSbgac() != null) {
                continue;
            }
            Accident accident = new Accident();
            accident.setMonth(lwswfx.getTjyf());
        }
    }

}
