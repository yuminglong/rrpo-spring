package com.jiebao.platfrom.railway.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jiebao.platfrom.common.domain.QueryRequest;
import com.jiebao.platfrom.common.domain.Tree;
import com.jiebao.platfrom.railway.domain.Address;
import com.jiebao.platfrom.system.domain.Dept;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @author yf
 */
public interface AddressService extends IService<Address> {
    /**
     * 分页查询通讯录
     * @param request
     * @return
     */
    IPage<Address> getAddressList(QueryRequest request);

    /**
     * 查询部门树
     * @param request
     * @param dept
     * @return
     */
    Map<String, Object> getAddressLists(QueryRequest request, Dept dept);

    /**
     * 修改通讯录
     * @param address
     */
    void updateByKey(Address address);

    /**
     *
     * @param request
     * @param address
     * @return
     */
    List<Address> findAddresses(QueryRequest request, Address address);

    boolean addAddressList(MultipartFile file, String deptId) throws Exception;

    List<Address> addressList(Address address, QueryRequest request);

    /**
     *
     * @param request
     * @param iPageDeptId
     * @param userName
     * @param telPhone
     * @return
     */
    IPage<Address> getByDept(QueryRequest request, String iPageDeptId, String userName, String telPhone);

    /**
     * 查询所处市级
     * @param deptRoot
     * @return
     */
    List<Address> getBookList(String deptRoot);
}
