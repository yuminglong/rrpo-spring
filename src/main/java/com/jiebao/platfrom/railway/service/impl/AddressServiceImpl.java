package com.jiebao.platfrom.railway.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiebao.platfrom.common.authentication.JWTUtil;
import com.jiebao.platfrom.common.domain.JiebaoConstant;
import com.jiebao.platfrom.common.domain.QueryRequest;
import com.jiebao.platfrom.common.domain.Tree;
import com.jiebao.platfrom.common.utils.SortUtil;
import com.jiebao.platfrom.common.utils.TreeUtil;
import com.jiebao.platfrom.railway.dao.AddressMapper;
import com.jiebao.platfrom.railway.domain.Address;
import com.jiebao.platfrom.railway.domain.Area;
import com.jiebao.platfrom.railway.domain.AreaTree;
import com.jiebao.platfrom.railway.domain.Inform;
import com.jiebao.platfrom.railway.service.AddressService;
import com.jiebao.platfrom.railway.service.AreaService;
import com.jiebao.platfrom.system.domain.Dept;
import com.jiebao.platfrom.system.service.DeptService;
import com.jiebao.platfrom.system.service.impl.DeptServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Service("AddressService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements AddressService {

    @Autowired
    AddressMapper addressMapper;

    @Autowired
    DeptService deptService;

    @Autowired
    AddressService addressService;

    @Autowired
    AreaService areaService;



    @Override
    public IPage<Address> getAddressList(QueryRequest request) {
        LambdaQueryWrapper<Address> lambdaQueryWrapper = new LambdaQueryWrapper();
        Page<Address> page = new Page<>(request.getPageNum(), request.getPageSize());
        lambdaQueryWrapper.orderByDesc(Address::getId);
        return this.baseMapper.selectPage(page, lambdaQueryWrapper);
    }

    @Override
    public Map<String, Object> getAddressLists(QueryRequest request, Dept dept) {

        Map<String, Object> result = new HashMap<>();
        try {
            List<Dept> depts = deptService.findDept(dept, request);
            //   List<Address> addresses = addressService.findAddresses(address, request);
            List<Tree<Dept>> trees = new ArrayList<>();
            buildTrees(trees, depts);
            Tree<Dept> deptTree = TreeUtil.builds(trees);
            result.put("rows", deptTree);
            result.put("total", depts.size());
        } catch (Exception e) {
            log.error("获取部门列表失败", e);
            result.put("rows", null);
            result.put("total", 0);
        }
        return result;
    }

    @Override
    public Map<String, Object> getAddressListsByArea(QueryRequest request, Area area) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Area> areas = areaService.getAreaList(request, area);
            //   List<Address> addresses = addressService.findAddresses(address, request);
            List<AreaTree<Area>> trees = new ArrayList<>();
            buildAreaTrees(trees, areas,request);
            AreaTree<Area> areaTree = TreeUtil.buildArea(trees);
            result.put("rows", areaTree);
            result.put("total", areas.size());
        } catch (Exception e) {
            log.error("获取部门列表失败", e);
            result.put("rows", null);
            result.put("total", 0);
        }
        return result;
    }



    @Override
    public List<Address> findAddresses(QueryRequest request, Address address) {
        QueryWrapper<Address> queryWrapper = new QueryWrapper<>();
        SortUtil.handleWrapperSort(request, queryWrapper, "parentsId", JiebaoConstant.ORDER_DESC, true);
        List<Address> addresses = baseMapper.selectList(queryWrapper);
        for (Address s : addresses) {
            String parentsId = s.getParentsId();
            Dept byId = deptService.getById(parentsId);
            s.setDeptName(byId.getDeptName());
        }
        return addresses;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateByKey(Address address) {
        this.addressMapper.updateById(address);
    }


    private void buildTrees(List<Tree<Dept>> trees, List<Dept> depts) {


        depts.forEach(dept -> {
            List<Address> address = addressMapper.getAddressList(dept.getDeptId());
            Tree<Dept> tree = new Tree<>();
            tree.setId(dept.getDeptId());
            tree.setKey(tree.getId());
            tree.setParentId(dept.getParentId());
            tree.setText(dept.getDeptName());
            tree.setCreateTime(dept.getCreateTime());
            tree.setModifyTime(dept.getModifyTime());
            tree.setOrder(dept.getOrderNum());
            tree.setTitle(tree.getText());
            tree.setValue(tree.getId());


            List<Tree<Dept>> childList = new ArrayList<>();
            for (int i = 0; i < address.size(); i++) {
                Tree<Dept> data = new Tree<>();
                Address info = address.get(i);
                data.setId(info.getId());
                data.setKey(info.getId());
                data.setWeiXin(info.getWeiXin());
                data.setUserName(info.getUserName());
                data.setOrder(info.getNumbers());
                data.setTitle(info.getDeptName());
                data.setCreateTime(info.getCreatTime());
                data.setPhone(info.getPhone());
                data.setTelPhone(info.getTelPhone());
                data.setEmail(info.getEmail());
                data.setParentId(info.getParentsId());
                childList.add(data);
            }
            tree.setChildren(childList);
            trees.add(tree);
        });
    }


    private void buildAreaTrees(List<AreaTree<Area>> trees, List<Area> areas,QueryRequest request) {


        areas.forEach(area -> {
            List<Address> address = addressMapper.getAddressListByArea(area.getId());
           // IPage<Address> address = addressService.getByArea(request, area.getId());
            AreaTree<Area> tree = new AreaTree<>();
            tree.setId(area.getId());
            tree.setKey(tree.getId());
            tree.setParentId(area.getParentId());
            tree.setAreaName(area.getAreaName());
            tree.setRank(area.getRank());
            tree.setAreaCode(area.getAreaCode());
            tree.setUpdateUser(area.getUpdateUser());
            tree.setCreatTime(area.getCreatTime());



            List<AreaTree<Area>> childList = new ArrayList<>();
            for (int i = 0; i < address.size(); i++) {
                AreaTree<Area> data = new AreaTree<>();
                Address info = address.get(i);
                data.setId(info.getId());
                data.setKey(info.getId());
                data.setWeiXin(info.getWeiXin());
                data.setUserName(info.getUserName());
                data.setCreatTime(info.getCreatTime());
                data.setPhone(info.getPhone());
                data.setTelPhone(info.getTelPhone());
                data.setEmail(info.getEmail());
                data.setParentId(info.getParentsId());
                childList.add(data);
            }
            tree.setChildren(childList);
            trees.add(tree);
        });
    }




    @Override
    public boolean addAddressList(MultipartFile file, String parentsId) throws Exception {
        boolean save = true;
        List<Address> addressList = new ArrayList<>();
        String filename = file.getOriginalFilename();
        String sub = filename.substring(filename.lastIndexOf(".") + 1);
        InputStream inputStream = file.getInputStream();
        Workbook wb = null;
        /**
         * HSSFWorkbook:是操作Excel2003以前（包括2003）的版本，扩展名是.xls
         * XSSFWorkbook:是操作Excel2007的版本，扩展名是.xlsx
         */
        if ("xlsx".equals(sub)) {
            wb = new XSSFWorkbook(inputStream);
        } else {
            wb = new HSSFWorkbook(inputStream);
        }
        Sheet sheet = wb.getSheetAt(0);
        if (sheet != null) {
            //getLastRowNum()返回最后一行的索引，即比行总数小1
            for (int line = 2; line <= sheet.getLastRowNum(); line++) {
                Address address = new Address();
                Row row = sheet.getRow(line);
                if (row == null) {
                    continue;
                }
                row.getCell(0).setCellType(CellType.STRING);
                if (CellType.STRING != row.getCell(0).getCellTypeEnum()) {
                    throw new Exception("单元格类型不是文本类型");
                }
                /**
                 * 获取第一个单元格的内容
                 */
                String userName = row.getCell(0).getStringCellValue();
                row.getCell(1).setCellType(CellType.STRING);
                row.getCell(2).setCellType(CellType.STRING);
                row.getCell(3).setCellType(CellType.STRING);
                row.getCell(4).setCellType(CellType.STRING);

                String phone = row.getCell(1).getStringCellValue();
                String telPhone = row.getCell(2).getStringCellValue();
                String weiXin = row.getCell(3).getStringCellValue();
                String email = row.getCell(4).getStringCellValue();
                address.setUserName(userName);
                address.setPhone(phone);
                address.setTelPhone(telPhone);
                address.setWeiXin(weiXin);
                address.setEmail(email);
                address.setParentsId(parentsId);
                addressList.add(address);
            }
            for (Address addressInfo : addressList) {
                String userName = addressInfo.getUserName();
                int count = addressService.selectUserName(userName);
                if (count == 0) {
                    save = addressService.save(addressInfo);
                }
            }
        }
        return save;
    }

    @Override
    public int selectUserName(String userName) {
        return addressMapper.selectUser(userName);
    }


    @Override
    public List<Address> addressList() {
        List<Address> list = addressService.list();
        for (Address address : list
        ) {
            String parentsId = address.getParentsId();
            Dept byId = deptService.getById(parentsId);
            address.setDeptName(byId.getDeptName());
        }
        return list;
    }


    @Override
    public List<Address> getByAreaId(String areaId) {
        return addressMapper.getByAreaId(areaId);
    }

    @Override
    public IPage<Address> getByArea(QueryRequest request,String iPageAreaId) {
        LambdaQueryWrapper<Address> lambdaQueryWrapper = new LambdaQueryWrapper();
        if (StringUtils.isNotBlank(iPageAreaId)) {
            lambdaQueryWrapper.eq(Address::getAreaId, iPageAreaId);
        }
        Page<Address> page = new Page<>(request.getPageNum(), request.getPageSize());
        lambdaQueryWrapper.orderByDesc(Address::getId);
        return this.baseMapper.selectPage(page, lambdaQueryWrapper);
    }
}
