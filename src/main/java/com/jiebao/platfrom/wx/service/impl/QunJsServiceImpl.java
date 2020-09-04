package com.jiebao.platfrom.wx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.wx.domain.QunJs;
import com.jiebao.platfrom.wx.dao.QunJsMapper;
import com.jiebao.platfrom.wx.service.IQunJsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
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
        if (qunId != null) {
            queryWrapper.eq("wx_id", qunId);
        }
        queryWrapper.orderByAsc("date");
        return new JiebaoResponse().data(list(queryWrapper)).message("查询成功");
    }

    @Override
    public JiebaoResponse addOrUpdate(QunJs entity) {
        JiebaoResponse jiebaoResponse = new JiebaoResponse();
        if (entity.getJsId() == null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            entity.setFillDate(simpleDateFormat.format(new Date()));
            if (!jude(entity.getWxId())) {
                return jiebaoResponse.failMessage("请勿重复绑定");
            }
        } else {
            QunJs qunJs = getById(entity.getJsId());
            if (!qunJs.getWxId().equals(entity.getWxId())) {
                if (!jude(entity.getWxId())) {
                    return jiebaoResponse.failMessage("请勿重复绑定");
                }
            }
        }
        jiebaoResponse = super.saveOrUpdate(entity) ? jiebaoResponse.okMessage("操作成功") : jiebaoResponse.failMessage("操作失败");
        return jiebaoResponse;
    }

    private boolean jude(String wxId) {
        return this.baseMapper.judge(wxId) == null ? true : false;
    }


}
