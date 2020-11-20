package com.jiebao.platfrom.mini.controller;

import com.jiebao.platfrom.common.domain.JiebaoConstant;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.service.CacheService;
import com.jiebao.platfrom.mini.entity.BookEntity;
import com.jiebao.platfrom.railway.domain.Address;
import com.jiebao.platfrom.railway.service.AddressService;
import com.jiebao.platfrom.system.domain.Dept;
import com.jiebao.platfrom.system.service.DeptService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping(value = "/mini/book")
@Api(tags = "小程序-通讯录")
public class MiniBookController {

    @Autowired
    private DeptService deptService;

    @Autowired
    private AddressService addressService;

    @Autowired
    CacheService cacheService;

    @GetMapping(value = "/getBookList")
    public JiebaoResponse getBookList (String deptId) throws Exception {
        List<Dept> deptList = cacheService.getAllChildrenDept(deptId);
        List<BookEntity> list = new ArrayList<>();
      /*  List<String> deptIdList=new ArrayList<>();
        for (Dept dept:deptList
             ) {
            deptIdList.add(dept.getDeptId());
        }
        List<Address> bookList = addressService.getList(deptIdList);*/
        deptList.forEach(dept -> {
            BookEntity bookEntity = new BookEntity();
            bookEntity.setId(dept.getDeptId());
            bookEntity.setName(dept.getDeptName());
            List<Address> bookList = addressService.getBookList(dept.getDeptId());
            bookEntity.setBookList(bookList);
            list.add(bookEntity);
        });
        return new JiebaoResponse().put("status", JiebaoConstant.STATUS_CODE_SUCCESS).put("list", list);
    }

}
