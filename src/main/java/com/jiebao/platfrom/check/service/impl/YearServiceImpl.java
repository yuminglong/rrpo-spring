package com.jiebao.platfrom.check.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiebao.platfrom.check.domain.Year;
import com.jiebao.platfrom.check.dao.YearMapper;
import com.jiebao.platfrom.check.service.IYearService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.domain.QueryRequest;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author qta
 * @since 2020-08-05
 */
@Service
public class YearServiceImpl extends ServiceImpl<YearMapper, Year> implements IYearService {


    @Override
    public JiebaoResponse addOrUpdate(Year year) {
        QueryWrapper<Year> queryWrapper = new QueryWrapper<>();
        if (year.getYearId() != null) {
            Year year1 = getById(year.getYearId());  //还未修改的 数据
            if (year1.getYearDate().equals(year.getYearDate())) {   //本次修改并未修改名字
                return new JiebaoResponse().message(super.saveOrUpdate(year) ? "操作成功" : "操作失败");
            }
        }
        queryWrapper.eq("year_date", year.getYearDate());
        if (getOne(queryWrapper) != null) {
            return new JiebaoResponse().message("年份重复");
        }
        return new JiebaoResponse().message(super.saveOrUpdate(year) ? "操作成功" : "操作失败");
    }

    @Override
    public List<Year> list(QueryWrapper<Year> queryWrapper) {
        return this.baseMapper.list(queryWrapper);
    }

    @Override
    public JiebaoResponse pageList(QueryRequest queryRequest, String yearDate) {
        QueryWrapper<Year> queryWrapper = new QueryWrapper<>();
        if (yearDate != null) {
            queryWrapper.eq("year_date", yearDate);
        }
        queryWrapper.orderByDesc("year_date");
        Page<Year> page = new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize());
        return new JiebaoResponse().data(this.baseMapper.pageYear(page, queryWrapper)).message("查询成功");
    }

    @Override
    public JiebaoResponse yearStringList() {
        return new JiebaoResponse().data(this.baseMapper.yearStringList()).message("查询成功");
    }


}
