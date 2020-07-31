package com.jiebao.platfrom.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jiebao.platfrom.system.domain.File;

import java.util.List;

public interface FileService extends IService<File> {

    List<File> getAppendixList(String refId);

}
