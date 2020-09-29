package com.jiebao.platfrom.railway.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiebao.platfrom.common.domain.JiebaoConstant;
import com.jiebao.platfrom.common.domain.QueryRequest;
import com.jiebao.platfrom.common.utils.SortUtil;
import com.jiebao.platfrom.railway.dao.AsYearMapper;
import com.jiebao.platfrom.railway.dao.PrizeLimitMapper;
import com.jiebao.platfrom.railway.domain.AsYear;
import com.jiebao.platfrom.railway.domain.PrizeLimit;
import com.jiebao.platfrom.railway.service.AsYearService;
import com.jiebao.platfrom.railway.service.PrizeLimitService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author yf
 */
@Slf4j
@Service("AsYearService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class AsYearServiceImpl extends ServiceImpl<AsYearMapper, AsYear> implements AsYearService {



}
