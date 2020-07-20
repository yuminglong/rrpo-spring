package com.jiebao.platfrom.railway.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jiebao.platfrom.common.domain.QueryRequest;
import com.jiebao.platfrom.railway.domain.Address;
import com.jiebao.platfrom.railway.domain.Files;

import java.util.List;

/**
 * @author yf
 */
public interface FileService extends IService<Files> {

    IPage<Files> getFileList(QueryRequest request);

    void updateByKey(Files files);

    List<Files> getByParentsId(String parentsId);

    List<Files> findByUser(String userId);
}
