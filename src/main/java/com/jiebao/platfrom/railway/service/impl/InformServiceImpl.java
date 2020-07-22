package com.jiebao.platfrom.railway.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiebao.platfrom.common.domain.JiebaoConstant;
import com.jiebao.platfrom.common.domain.QueryRequest;
import com.jiebao.platfrom.common.utils.SortUtil;
import com.jiebao.platfrom.railway.dao.InformMapper;
import com.jiebao.platfrom.railway.domain.Inform;
import com.jiebao.platfrom.railway.service.InformService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.List;


@Slf4j
@Service("InformService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class InformServiceImpl extends ServiceImpl<InformMapper, Inform> implements InformService {

    @Autowired
    InformMapper informMapper;

    @Override
    public IPage<Inform> getInformList(QueryRequest request, Inform inform, String startTime, String endTime) {
        QueryWrapper<Inform> queryWrapper = new QueryWrapper();

        if (StringUtils.isNotBlank(inform.getTitle())) {
            queryWrapper.lambda().eq(Inform::getTitle, inform.getTitle());
        }
        if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank( endTime)) {
            queryWrapper.lambda().ge(Inform::getCreateTime, startTime).le(Inform::getCreateTime, endTime);
        }
        Page<Inform> page = new Page<>(request.getPageNum(), request.getPageSize());
        SortUtil.handleWrapperSort(request, queryWrapper, "createTime", JiebaoConstant.ORDER_DESC, true);
        return this.baseMapper.selectPage(page, queryWrapper);
    }


    @Override
    @Transactional
    public void updateByKey(Inform inform) {
        this.informMapper.updateById(inform);
    }

    @Override
    public List<Inform> getInformLists(Inform inform, QueryRequest request) {
        QueryWrapper<Inform> queryWrapper = new QueryWrapper();
        if (StringUtils.isNotBlank(inform.getTitle())) {
            queryWrapper.lambda().eq(Inform::getTitle, inform.getTitle());
        }
        SortUtil.handleWrapperSort(request, queryWrapper, "createTime", JiebaoConstant.ORDER_DESC, true);
        return this.baseMapper.selectList(queryWrapper);
    }


    @Override
    public boolean revoke(String informId) {
        return informMapper.revoke(informId);
    }

    @Override
    public boolean release(String informId) {
        return informMapper.release(informId);
    }
}
