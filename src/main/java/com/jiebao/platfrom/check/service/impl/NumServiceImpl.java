package com.jiebao.platfrom.check.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiebao.platfrom.check.domain.Num;
import com.jiebao.platfrom.check.dao.NumMapper;
import com.jiebao.platfrom.check.service.INumService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.domain.QueryRequest;
import com.jiebao.platfrom.system.domain.User;
import com.jiebao.platfrom.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    @Autowired
    UserService userService;

    @Override
    public JiebaoResponse pageList(QueryRequest queryRequest, String deptId, String dateYear) {
        QueryWrapper<Num> queryWrapper = new QueryWrapper<>();
        if (deptId != null) {
            queryWrapper.eq("dept_id", deptId);
        }
        if (dateYear != null) {
            queryWrapper.eq("year_date", dateYear);
        }
        queryWrapper.orderByDesc("year_date");
        Page<Num> page = new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize());
        return new JiebaoResponse().data(this.baseMapper.pageList(page, queryWrapper)).message("查询成功");
    }
}
