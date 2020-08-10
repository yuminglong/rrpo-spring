package com.jiebao.platfrom.railway.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiebao.platfrom.railway.dao.PrizeTypeMapper;
import com.jiebao.platfrom.railway.dao.PublicFileMapper;
import com.jiebao.platfrom.railway.domain.PrizeType;
import com.jiebao.platfrom.railway.domain.PublicFile;
import com.jiebao.platfrom.railway.service.PrizeTypeService;
import com.jiebao.platfrom.railway.service.PublicFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author yf
 */
@Slf4j
@Service("PrizeFileService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PublicFileServiceImpl extends ServiceImpl<PublicFileMapper, PublicFile> implements PublicFileService {


}
