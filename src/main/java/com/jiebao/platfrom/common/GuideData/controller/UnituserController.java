package com.jiebao.platfrom.common.GuideData.controller;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.jiebao.platfrom.common.GuideData.domain.Unituser;
import com.jiebao.platfrom.common.GuideData.service.UnitUserService;
import com.jiebao.platfrom.common.controller.BaseController;
import com.jiebao.platfrom.common.dataImport.domain.Info;
import com.jiebao.platfrom.common.dataImport.service.InfoService;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.exception.JiebaoException;
import com.jiebao.platfrom.common.utils.MD5Util;
import com.jiebao.platfrom.system.domain.User;
import com.jiebao.platfrom.system.service.UserService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.jar.JarException;

@Slf4j
@Validated
@RestController
@RequestMapping("/Unituser")
@Api(tags = "railWay-导数据")   //swagger2 api文档说明示例
public class UnituserController extends BaseController {

    private String message;

    @Autowired
    private UnitUserService unituserService;

    @Autowired
    private UserService userService;

    @Autowired
    private InfoService infoService;

    @GetMapping
    public JiebaoResponse getList() {
        List<Unituser> list = unituserService.list();
        for (Unituser oldUser: list
             ) {
            User user = new User();
            user.setUsername(oldUser.getUnitUser());
            user.setMobile(oldUser.getMobileTelephone());
            user.setStatus("1");
            user.setCreateTime(new Date());
            user.setModifyTime(new Date());
            user.setDescription(oldUser.getHeadship());
            user.setAvatar("default.jpg");
            user.setType(0);
            user.setSsex("2");
            user.setPassword(MD5Util.encrypt(oldUser.getUnitUser(), Unituser.DEFAULT_PASSWORD));
            userService.save(user);
        }
        return new JiebaoResponse().data(list);
    }


    @GetMapping("/info")
    public JiebaoResponse Info() {
        List<Info> list = infoService.list();
        System.out.println(list);
        return new JiebaoResponse().data(list);

    }


}
