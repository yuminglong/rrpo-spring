package com.jiebao.platfrom.railway.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jiebao.platfrom.common.domain.QueryRequest;
import com.jiebao.platfrom.railway.domain.PrivateFile;

import java.util.List;
import java.util.Map;

/**
 * @author yf
 */
public interface PrivateFileService extends IService<PrivateFile> {


   Map<String, Object> findPrivateFileList(QueryRequest request, PrivateFile privateFile);

   List<PrivateFile> findPrivateFiles(PrivateFile privateFile, QueryRequest request);

   void createPrivateFile(PrivateFile privateFile);

   List<PrivateFile> findChilderPrivateFile(String id);

   List<PrivateFile> getPrivateFileListById(String privateFileId);
}
