package com.jiebao.platfrom.wx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiebao.platfrom.common.authentication.JWTUtil;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.domain.QueryRequest;
import com.jiebao.platfrom.demo.test.WorderToNewWordUtils;
import com.jiebao.platfrom.railway.domain.Address;
import com.jiebao.platfrom.system.dao.UserMapper;
import com.jiebao.platfrom.system.domain.Dept;
import com.jiebao.platfrom.system.domain.File;
import com.jiebao.platfrom.system.service.DeptService;
import com.jiebao.platfrom.system.service.FileService;
import com.jiebao.platfrom.system.service.UserService;
import com.jiebao.platfrom.wx.domain.Month;
import com.jiebao.platfrom.wx.dao.MonthMapper;
import com.jiebao.platfrom.wx.service.IMonthService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.hibernate.validator.internal.util.privilegedactions.GetResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author qta
 * @since 2020-08-22
 */
@Service
public class MonthServiceImpl extends ServiceImpl<MonthMapper, Month> implements IMonthService {
    @Autowired
    DeptService deptService;
    @Autowired
    UserMapper userMapper;
    @Autowired
    FileService fileService;


    @Override
    public boolean saveOrUpdate(Month entity) {
        if (entity.getWxMonthId() == null) {
            String username = JWTUtil.getUsername(SecurityUtils.getSubject().getPrincipal().toString());
            Dept dept = deptService.getById(userMapper.getDeptID(username));  //当前登陆人的部门
            entity.setJcDeptId(dept.getDeptId());
            entity.setShDeptId(dept.getDeptId());
            entity.setDate(new Date());
        }
        boolean b = super.saveOrUpdate(entity);
        if (b && entity.getFileIds() != null && entity.getFileIds().length != 0) {
            UpdateWrapper<File> updateWrapper = new UpdateWrapper<>();
            updateWrapper.in("file_id", Arrays.asList(entity.getFileIds()));
            updateWrapper.set("ref_id", entity.getWxMonthId());
            fileService.update(updateWrapper);
        }
        return b;
    }

    @Override
    public JiebaoResponse pageList(QueryRequest queryRequest, String month, Integer look, Integer status) {
        QueryWrapper<Month> queryWrapper = new QueryWrapper<>();
        String username = JWTUtil.getUsername(SecurityUtils.getSubject().getPrincipal().toString());
        Dept dept = deptService.getById(userMapper.getDeptID(username));  //当前登陆人的部门
        List<Dept> childrenList = deptService.getChildrenList(dept.getDeptId());//当前部门的所有子集部门
        List<String> resolver = resolver(childrenList);
        if (resolver.size() != 0) {
            queryWrapper.and(monthQueryWrapper -> monthQueryWrapper.eq("jc_dept_id", dept.getDeptId()).or().eq("sh_dept_id", dept.getDeptId())
                    .or().in("jc_dept_id", resolver).eq("status", 1));
        } else {
            queryWrapper.and(monthQueryWrapper -> monthQueryWrapper.eq("jc_dept_id", dept.getDeptId()).or().eq("sh_dept_id", dept.getDeptId())
            );
        }
        if (month != null) {
            queryWrapper.eq("month", month);
        }
        if (status != null) {
            queryWrapper.eq("status", status);
        }
        if (look != null) {
            queryWrapper.eq("look", look);
        }
        queryWrapper.orderByDesc("date");
        Page<Month> page = new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize());
        return new JiebaoResponse().data(this.baseMapper.list(page, queryWrapper)).message("查询成功");
    }

    private List<String> resolver(List<Dept> list) {
        List<String> listR = new ArrayList<>(); //储存数据
        for (Dept dept : list
        ) {
            listR.add(dept.getDeptId());
        }
        return listR;
    }

    @Override
    public JiebaoResponse appear(String monthId, Integer status) {
        String username = JWTUtil.getUsername(SecurityUtils.getSubject().getPrincipal().toString());
        Dept dept = deptService.getById(userMapper.getDeptID(username));  //当前登陆人的部门
        JiebaoResponse jiebaoResponse = new JiebaoResponse();
        Month month = getById(monthId);
        if (!month.getShDeptId().equals(dept.getDeptId())) {
            return jiebaoResponse.failMessage("无权上报");
        }
//        if (dept.getParentId().equals("0")) {  //此为 市级
//            if (this.baseMapper.count(month.getMonth(), dept.getDeptId()) >= 3) {
//                return jiebaoResponse.failMessage("超过上报三条记录上限");
//            }
//        }
        if (dept.getParentId().equals("0")) { //此为市级
            month.setSzDeptName(dept.getDeptName());//保存市级单位   做报表要用
        }
        if (status == 1) {  //赞成  //
            if (dept.getParentId().equals("-1")) {//省级
                month.setStatus(1);
            } else {  //非省级 继续上报
                month.setStatus(2);
                month.setShDeptId(dept.getParentId());
            }
        } else {
            month.setStatus(0);
        }
        updateById(month);
        return jiebaoResponse.okMessage("操作成功");
    }

    @Override
    public JiebaoResponse koran(String MonthId, Integer status) {
        JiebaoResponse jiebaoResponse = new JiebaoResponse();
        UpdateWrapper<Month> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("pre_status", status);
        updateWrapper.eq("wx_month_id", MonthId);
        jiebaoResponse = update(updateWrapper) ? jiebaoResponse.okMessage("操作成功") : jiebaoResponse.failMessage("操作失败");
        return jiebaoResponse;
    }

    @Override
    public void monthDocx(HttpServletResponse response, String month) {
        String inputUrl = GetResource.class.getClassLoader().getResource("month.docx").getPath();//模板位置
        String newName = UUID.randomUUID().toString();
        String outputUrl = "D:\\upload\\words\\" + newName;
        String outPath = outputUrl + ".docx";  //导出地址
        Map<String, String> map = new HashMap<>();
        map.put("month", month+"全省乡镇街微信护路推荐汇总表");
        ArrayList<String[]> list = new ArrayList<>();
        for (Month Month : listByMonth(month)
        ) {
            Dept dept = deptService.getById(Month.getJcDeptId()); //发起最小单位
            if (Month.getPreStatus() == null) {
                Month.setPreStatus(0);
            }
            list.add(new String[]{Month.getSerial().toString(), Month.getSzDeptName(), dept.getDeptName(), Month.getContent(), Month.getPreStatus() == 1 ? "可入" : "不可入"});
        }
        WorderToNewWordUtils.changWordMonth(response,inputUrl, newName, map, list);

    }

    private List<Month> listByMonth(String month) {
        QueryWrapper<Month> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("month", month);
        return list(queryWrapper);
    }

    @Override
    public JiebaoResponse monthDocxText(QueryRequest queryRequest, String month) {
        QueryWrapper<Month> queryWrapper = new QueryWrapper<>();
        if (month != null) {
            queryWrapper.eq("month", month);
        }
        Page<Month> page = new Page<>(queryRequest.getPageSize(), queryRequest.getPageNum());
        return new JiebaoResponse().data(page(page, queryWrapper)).message("查询成功");
    }


    @Override
    public Month getById(Serializable id) {
        Month month = super.getById(id);
        month.setLook(1);
        updateById(month);
        month.setFileList(fileService.getAppendixList(month.getWxMonthId()));
        return month;
    }
}
