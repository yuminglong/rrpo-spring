package com.jiebao.platfrom.accident.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiebao.platfrom.accident.daomain.Accident;
import com.jiebao.platfrom.accident.dao.AccidentMapper;
import com.jiebao.platfrom.accident.service.IAccidentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.domain.QueryRequest;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author qta
 * @since 2020-08-04
 */
@Service
public class AccidentServiceImpl extends ServiceImpl<AccidentMapper, Accident> implements IAccidentService {

    @Override
    public JiebaoResponse list(QueryRequest queryRequest, String cityCsId, String cityQxId, String startDate, String endDate) {
        QueryWrapper<Accident> queryWrapper = new QueryWrapper<>();
        if (cityQxId != null) {
            queryWrapper.eq("city_qx_id", cityQxId);
        } else if (cityCsId != null) {
            queryWrapper.eq("city_cs_id", cityCsId);
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (startDate != null) {
            try {
                queryWrapper.ge("date", simpleDateFormat.parse(startDate));    //不能小于此时间
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (endDate != null) {
            try {
                queryWrapper.le("date", simpleDateFormat.parse(endDate));//不能大于此时间
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        Page<Accident> page = new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize());
        return new JiebaoResponse().data(this.baseMapper.ListPage(page, queryWrapper)).message("查询成功");
    }


}
