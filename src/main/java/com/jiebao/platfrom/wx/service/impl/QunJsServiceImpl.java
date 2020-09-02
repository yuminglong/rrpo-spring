package com.jiebao.platfrom.wx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.wx.domain.QunJs;
import com.jiebao.platfrom.wx.dao.QunJsMapper;
import com.jiebao.platfrom.wx.service.IQunJsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author qta
 * @since 2020-08-31
 */
@Service
public class QunJsServiceImpl extends ServiceImpl<QunJsMapper, QunJs> implements IQunJsService {

    @Override
    public JiebaoResponse selectById(String qunId) {
        QueryWrapper<QunJs> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("qun_id", qunId);
        queryWrapper.orderByAsc("date");
        return new JiebaoResponse().data(list(queryWrapper)).message("查询成功");
    }

    @Override
    public boolean saveOrUpdate(QunJs entity) {
        entity.setDate(new Date());
        return super.saveOrUpdate(entity);
    }
}
