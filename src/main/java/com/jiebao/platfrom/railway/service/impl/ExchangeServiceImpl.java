package com.jiebao.platfrom.railway.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiebao.platfrom.common.domain.QueryRequest;
import com.jiebao.platfrom.railway.dao.ExchangeFileMapper;
import com.jiebao.platfrom.railway.dao.ExchangeMapper;
import com.jiebao.platfrom.railway.domain.Exchange;
import com.jiebao.platfrom.railway.domain.ExchangeFile;
import com.jiebao.platfrom.railway.service.ExchangeFileService;
import com.jiebao.platfrom.railway.service.ExchangeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Slf4j
@Service("ExchangeService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ExchangeServiceImpl extends ServiceImpl<ExchangeMapper, Exchange> implements ExchangeService {


}
