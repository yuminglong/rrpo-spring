package com.jiebao.platfrom.railway.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jiebao.platfrom.common.domain.QueryRequest;
import com.jiebao.platfrom.railway.domain.Exchange;
import com.jiebao.platfrom.railway.domain.ExchangeFile;

import java.util.List;

/**
 * @author yf
 */
public interface ExchangeService extends IService<Exchange> {

    IPage<Exchange> getExchangeList(QueryRequest request, Exchange exchange, String startTime, String endTime);

    IPage<Exchange> getExchangeInboxList(QueryRequest request, Exchange exchange, String startTime, String endTime);
}
