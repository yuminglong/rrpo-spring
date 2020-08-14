package com.jiebao.platfrom.railway.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jiebao.platfrom.railway.domain.PrizeType;
import com.jiebao.platfrom.railway.domain.PublicFile;

/**
 * @author yf
 */
public interface PublicFileService extends IService<PublicFile> {



   void createPublicFile(PublicFile publicFile);
}
