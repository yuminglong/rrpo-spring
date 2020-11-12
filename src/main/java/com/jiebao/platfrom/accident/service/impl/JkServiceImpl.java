package com.jiebao.platfrom.accident.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiebao.platfrom.accident.daomain.CountTable;
import com.jiebao.platfrom.accident.daomain.Jk;
import com.jiebao.platfrom.accident.dao.JkMapper;
import com.jiebao.platfrom.accident.daomain.Ql;
import com.jiebao.platfrom.accident.service.IJkService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiebao.platfrom.common.domain.QueryRequest;
import com.jiebao.platfrom.common.utils.ExportExcel;
import com.jiebao.platfrom.system.domain.File;
import com.jiebao.platfrom.system.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author qta
 * @since 2020-11-07
 */
@Service
public class JkServiceImpl extends ServiceImpl<JkMapper, Jk> implements IJkService {
    @Autowired
    FileService fileService;

    @Override
    public IPage<Jk> listPage(QueryRequest queryRequest, String gac, String dzs) {
        Page<Jk> page = new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize());
        return this.baseMapper.pageList(page, queryWrapper(gac, dzs));
    }

    @Override
    public boolean importExcel(HttpServletResponse response, String gac, String dzs) {
        return ExportExcel.exportExcelList(list(queryWrapper(gac, dzs)), Jk.class, response);
    }

    private LambdaQueryWrapper<Jk> queryWrapper(String gac, String dzs) {
        LambdaQueryWrapper<Jk> queryWrapper = new LambdaQueryWrapper<>();
        if (gac != null)
            queryWrapper.eq(Jk::getGac, gac);
        if (dzs != null)
            queryWrapper.eq(Jk::getDzs, dzs);
        queryWrapper.eq(Jk::getDelflag, "0");
        queryWrapper.orderByDesc(Jk::getCreatTime);
        queryWrapper.orderByDesc(Jk::getGac);
        queryWrapper.orderByDesc(Jk::getXsq);
        queryWrapper.orderByDesc(Jk::getDzs);
        queryWrapper.orderByDesc(Jk::getXzjd);
        return queryWrapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addOrUpdate(Jk jk, Collection<? extends Serializable> tp) {
        boolean flag = false;  //记录是新增 还是  删除
        if (jk.getId() == null) {
            jk.setCreatTime(LocalDateTime.now());
            jk.setDelflag("0");
            flag = true;
        }
        boolean b = saveOrUpdate(jk);
        if (tp != null && flag) {  //绑定文件
            LambdaUpdateWrapper<File> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.set(File::getRefId, jk.getId());
            updateWrapper.in(File::getFileId, tp);
            fileService.update(updateWrapper);
        }
        return b;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(String[] idList) {
        LambdaUpdateWrapper<Jk> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(Jk::getDelflag, 1);
        updateWrapper.in(Jk::getId, Arrays.asList(idList));
        return update(updateWrapper);
    }

    @Override
    public List<CountTable> countTable(String deptName) {
        String columnName = "";
        if (deptName != null)
            columnName = "dzs";
        else
            columnName = "gac";
        System.out.println(deptName);
        return this.baseMapper.countTable(columnName, deptName);
    }
}
