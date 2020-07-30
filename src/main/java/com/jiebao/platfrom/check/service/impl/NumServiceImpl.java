package com.jiebao.platfrom.check.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiebao.platfrom.check.domain.Num;
import com.jiebao.platfrom.check.dao.NumMapper;
import com.jiebao.platfrom.check.service.INumService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.domain.QueryRequest;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author qta
 * @since 2020-07-28
 */
@Service
public class NumServiceImpl extends ServiceImpl<NumMapper, Num> implements INumService {

    @Override
    public JiebaoResponse pageList(QueryRequest queryRequest, String userId, String deptId, Date dateYear) {
        QueryWrapper<Num> queryWrapper = new QueryWrapper<>();
        if (deptId != null) {
            queryWrapper.eq("dept_id", deptId);
        } else {
            if (userId != null) {
                queryWrapper.eq("user_id", deptId);
            }
        }
        if (dateYear != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
            String formatYear = simpleDateFormat.format(dateYear);
            queryWrapper.eq("year_date", formatYear);
        }
        Page<Num> page = new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize());
        return new JiebaoResponse().data(page(page, queryWrapper)).message("查询成功");
    }
}
