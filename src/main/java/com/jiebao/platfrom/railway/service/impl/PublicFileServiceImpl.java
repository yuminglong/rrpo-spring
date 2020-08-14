package com.jiebao.platfrom.railway.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiebao.platfrom.railway.dao.PrizeTypeMapper;
import com.jiebao.platfrom.railway.dao.PublicFileMapper;
import com.jiebao.platfrom.railway.domain.PrizeType;
import com.jiebao.platfrom.railway.domain.PublicFile;
import com.jiebao.platfrom.railway.service.PrizeTypeService;
import com.jiebao.platfrom.railway.service.PublicFileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;


/**
 * @author yf
 */
@Slf4j
@Service("PrizeFileService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PublicFileServiceImpl extends ServiceImpl<PublicFileMapper, PublicFile> implements PublicFileService {


    @Override
    @Transactional
    public void createPublicFile(PublicFile publicFile) {
        String parentId = publicFile.getParentId();
        if (StringUtils.isEmpty(parentId)){
            publicFile.setParentId("0");
        }
        this.save(publicFile);
    }
}
