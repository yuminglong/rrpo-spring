package com.jiebao.platfrom.accident.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiebao.platfrom.accident.daomain.Jk;
import com.jiebao.platfrom.accident.daomain.Ql;
import com.jiebao.platfrom.accident.dao.QlMapper;
import com.jiebao.platfrom.accident.service.IQlService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.domain.QueryRequest;
import com.jiebao.platfrom.common.utils.ExportExcel;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author qta
 * @since 2020-11-07
 */
@Service
public class QlServiceImpl extends ServiceImpl<QlMapper, Ql> implements IQlService {


    @Override
    public IPage<Ql> listPage(QueryRequest queryRequest, String deptName, String policeName, String gwdName) {
        Page<Ql> page = new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize());
        return page(page, queryWrapper(deptName, policeName, gwdName));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean importExcel(HttpServletResponse response, String deptName, String policeName, String gwdName) {
        return ExportExcel.exportExcelList(list(queryWrapper(deptName, policeName, gwdName)),Ql.class,response);
    }

    private LambdaQueryWrapper<Ql> queryWrapper(String deptName, String policeName, String gwdName) {
        LambdaQueryWrapper<Ql> queryWrapper = new LambdaQueryWrapper<>();
        if (deptName != null)
            queryWrapper.eq(Ql::getDzs, deptName);
        if (policeName != null)
            queryWrapper.eq(Ql::getGa, policeName);
        if (gwdName != null)
            queryWrapper.eq(Ql::getGwd, gwdName);
        queryWrapper.orderByDesc(Ql::getCreatTime);
        return queryWrapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addOrUpdate(Ql ql) {
        if(ql.getId()==null)
            ql.setCreatTime(LocalDateTime.now());
        return saveOrUpdate(ql);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(String[] idList) {
        LambdaUpdateWrapper<Ql> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(Ql::getDelflag, 1);
        updateWrapper.in(Ql::getId, Arrays.asList(idList));
        return update(updateWrapper);
    }


}

