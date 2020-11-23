package com.jiebao.platfrom.mini.controller;

import com.jiebao.platfrom.common.domain.JiebaoConstant;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.service.CacheService;
import com.jiebao.platfrom.mini.entity.BookEntity;
import com.jiebao.platfrom.railway.dao.AddressMapper;
import com.jiebao.platfrom.railway.domain.Address;
import com.jiebao.platfrom.railway.service.AddressService;
import com.jiebao.platfrom.system.dao.DeptMapper;
import com.jiebao.platfrom.system.domain.Dept;
import com.jiebao.platfrom.system.service.DeptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
    private DeptMapper deptMapper;

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    CacheService cacheService;

    @GetMapping(value = "/getBookList")
    @ApiOperation(value = "根据组织机构ID查询本级所有人员", notes = "根据组织机构ID查询本级所有人员", response = JiebaoResponse.class, httpMethod = "GET")
    public JiebaoResponse getBookList(String deptId)  {
            Map<String, Object> map = new HashMap<>();
            map.put("dept_id", deptId);
            List<Address> addresses = addressMapper.selectByMap(map);
            return new JiebaoResponse().put("status", JiebaoConstant.STATUS_CODE_SUCCESS).put("list", addresses);
    }

    @GetMapping(value = "/getBookDeptList")
    @ApiOperation(value = "根据组织机构ID查询下级", notes = "根据组织机构ID查询下级", response = JiebaoResponse.class, httpMethod = "GET")
    public JiebaoResponse getBookDeptList(String deptId)  {
        Map<String, Object> map = new HashMap<>();
        map.put("parent_id", deptId);
        List<Dept> depts = deptMapper.selectByMap(map);
        return new JiebaoResponse().put("status", JiebaoConstant.STATUS_CODE_SUCCESS).put("list", depts);
    }
}
