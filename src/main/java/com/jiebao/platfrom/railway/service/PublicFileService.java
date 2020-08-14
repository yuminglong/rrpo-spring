package com.jiebao.platfrom.railway.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jiebao.platfrom.common.domain.QueryRequest;
import com.jiebao.platfrom.railway.domain.PrizeType;
import com.jiebao.platfrom.railway.domain.PublicFile;
import com.jiebao.platfrom.system.domain.Dept;

import java.util.List;
import java.util.Map;

/**
 * @author yf
 */
public interface PublicFileService extends IService<PublicFile> {


   Map<String, Object> findpublicFileList(QueryRequest request, PublicFile publicFile);

   List<PublicFile> findPublicFiles(PublicFile publicFile, QueryRequest request);

   void createPublicFile(PublicFile publicFile);

   List<PublicFile> findChilderPublicFile(String id);
}
