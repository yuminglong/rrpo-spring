package com.jiebao.platfrom.railway.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiebao.platfrom.common.annotation.Log;
import com.jiebao.platfrom.common.authentication.JWTUtil;
import com.jiebao.platfrom.common.controller.BaseController;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.domain.QueryRequest;
import com.jiebao.platfrom.common.exception.JiebaoException;
import com.jiebao.platfrom.railway.dao.AddressMapper;
import com.jiebao.platfrom.railway.domain.Address;
import com.jiebao.platfrom.railway.service.AddressService;
import com.jiebao.platfrom.system.dao.DeptMapper;
import com.jiebao.platfrom.system.domain.Dept;
import com.jiebao.platfrom.system.domain.User;
import com.jiebao.platfrom.system.service.UserService;
import com.wuwenze.poi.ExcelKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * @author yf
 */
@Slf4j
@RestController
@RequestMapping(value = "address")
@Api(tags = "railWay-通讯录")   //swagger2 api文档说明示例
public class AddressController extends BaseController {

    private String message;

    @Autowired
    private AddressService addressService;

    @Autowired
    private DeptMapper deptMapper;


    /**
     * 使用Mapper操作数据库
     *
     * @return JiebaoResponse 标准返回数据类型
     */
    @GetMapping
    @ApiOperation(value = "根据组织机构查询数据List", notes = "查询数据List列表", response = JiebaoResponse.class, httpMethod = "GET")
    public Map<String, Object> getAddressListByMapper(QueryRequest request, Dept dept) {
       /* String username = JWTUtil.getUsername((String) SecurityUtils.getSubject().getPrincipal());
        System.out.println("-------------------"+username+"-------------------");
        //Object principal = SecurityUtils.getSubject().getPrincipal();
        String id = JWTUtil.getId((String) SecurityUtils.getSubject().getPrincipal());
        System.out.println("-------------------"+id+"-------------------");*/
        return this.addressService.getAddressLists(request, dept);
    }


    /**
     * 分页查询
     *
     * @return
     * @Parameters sortField  according to order by Field
     * @Parameters sortOrder  JiebaoConstant.ORDER_ASC or JiebaoConstant.ORDER_DESC
     */
    @DeleteMapping("/{ids}")
    @Log("删除")
    @Transactional(rollbackFor = Exception.class)
    @ApiOperation(value = "批量删除", notes = "批量删除", httpMethod = "DELETE")
    public JiebaoResponse delete(@PathVariable String[] ids) throws JiebaoException {
        try {
            Arrays.stream(ids).forEach(id -> {
                addressService.removeById(id);
            });
        } catch (Exception e) {
            throw new JiebaoException("删除失败");
        }
        return new JiebaoResponse().message("删除成功");
    }

    @PostMapping
    @Log("新增")
    @ApiOperation(value = "新增通讯录", notes = "新增通讯录", httpMethod = "POST")
    @Transactional(rollbackFor = Exception.class)
    public void addAddress(@Valid Address address) throws JiebaoException {
        try {
            address.setStatus(1);
            this.addressService.saveOrUpdate(address);
        } catch (Exception e) {
            message = "新增通讯录失败";
            log.error(message, e);
            throw new JiebaoException(message);
        }
    }


    @PutMapping
    @Log("修改通讯录")
    @ApiOperation(value = "修改通讯录", notes = "修改通讯录", httpMethod = "PUT")
    @Transactional(rollbackFor = Exception.class)
    public void updateAddress(@Valid Address address) throws JiebaoException {
        try {
            this.addressService.updateByKey(address);
        } catch (Exception e) {
            message = "修改通讯录失败";
            log.error(message, e);
            throw new JiebaoException(message);
        }
    }


    @PostMapping(value = "/importAddress")
    @ApiOperation(value = "导入通讯录", notes = "导入通讯录", httpMethod = "POST")
    public String excelImport(@RequestParam(value = "file") MultipartFile file, String deptId) {
        boolean result = false;
        try {
                result = addressService.addAddressList(file, deptId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result == true) {
            return "excel文件数据导入成功！";
        } else {
            return "excel数据导入失败！";
        }
    }


    @PostMapping(value = "/excel")
    @ApiOperation(value = "导出", notes = "导出", httpMethod = "POST")
    public void export(HttpServletResponse response, QueryRequest request, Address address) throws JiebaoException {
        try {
            List<Address> addresses = this.addressService.addressList(address, request);
            for (Address addr : addresses
            ) {

                if (addr.getDeptId() == null) {
                    addr.setDeptName("");
                } else {
                    Dept byId = deptMapper.getById(addr.getDeptId());
                    if (byId == null) {
                        addr.setDeptName("");
                    } else {
                        addr.setDeptName(byId.getDeptName());
                    }
                }
            }
            ExcelKit.$Export(Address.class, response).downXlsx(addresses, false);
        } catch (Exception e) {
            message = "导出Excel失败";
            log.error(message, e);
            throw new JiebaoException(message);
        }
    }


    @GetMapping("/iPage")
    @Log("根据组织机构分页查看通讯录")
    @ApiOperation(value = "根据组织机构分页查看通讯录", notes = "根据组织机构分页查看通讯录", httpMethod = "GET")
    @Transactional(rollbackFor = Exception.class)
    public JiebaoResponse findByDept(QueryRequest request, String iPageDeptId, String userName, String telPhone) {
        IPage<Address> deptList = addressService.getByDept(request, iPageDeptId, userName, telPhone);
        return new JiebaoResponse().data(this.getDataTable(deptList));
    }
}
