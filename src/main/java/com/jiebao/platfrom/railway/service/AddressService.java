package com.jiebao.platfrom.railway.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jiebao.platfrom.common.domain.QueryRequest;
import com.jiebao.platfrom.common.domain.Tree;
import com.jiebao.platfrom.railway.domain.Address;
import com.jiebao.platfrom.railway.domain.Area;
import com.jiebao.platfrom.system.domain.Dept;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @author yf
 */
public interface AddressService extends IService<Address> {

    IPage<Address> getAddressList(QueryRequest request);

    Map<String, Object> getAddressLists(QueryRequest request, Dept dept);

    Map<String, Object> getAddressListsByArea(QueryRequest request, Area area);

    void updateByKey(Address address);

    List<Address> findAddresses(QueryRequest request, Address address);

    boolean addAddressList(MultipartFile file, String parentsId) throws Exception;

    int selectUserName(String userName);

    List<Address> addressList();

    List<Address> getByAreaId(String areaId);

    IPage<Address> getByArea(QueryRequest request,String iPageAreaId,String userName,String telPhone);

    List<Address> getAddressByCondition(String userName,String phone,String areaId);

}
