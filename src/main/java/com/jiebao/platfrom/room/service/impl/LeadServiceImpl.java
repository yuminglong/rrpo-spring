package com.jiebao.platfrom.room.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiebao.platfrom.common.domain.QueryRequest;
import com.jiebao.platfrom.room.dao.LeadMapper;
import com.jiebao.platfrom.room.domain.Lead;
import com.jiebao.platfrom.room.service.ILeadService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author qta
 * @since 2020-07-20
 */
@Service
public class LeadServiceImpl extends ServiceImpl<LeadMapper, Lead> implements ILeadService {

    @Override
    public Boolean addLead(String id, List<String> LeadListId) {  //绑定 会议 领导
        List<Lead> list = new ArrayList<>();  //储存对象
        QueryWrapper<Lead> queryWrapper = null;
        for (String leadId : LeadListId
        ) {
            Lead lead = new Lead();
            lead.setMettingId(id);
            lead.setUserLeadId(leadId);
            queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("metting_id", id);
            queryWrapper.eq("user_lead_id", leadId);
            if (this.baseMapper.selectOne(queryWrapper) == null) { //本对象在数据库不存在执行添加
                list.add(lead);
            }
        }
        return saveBatch(list);
    }

    @Override
    public Boolean deleteByListId(String id, List<String> idList) {
        QueryWrapper<Lead> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("user_lead_id", idList);
        queryWrapper.eq("metting_id", id);
        this.baseMapper.delete(queryWrapper);
        return null;
    }

    @Override
    public IPage<Lead> list(QueryRequest request, String id) {
        QueryWrapper<Lead> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("metting_id", id);
        Page<Lead> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.baseMapper.selectPage(page, queryWrapper);
    }
}
