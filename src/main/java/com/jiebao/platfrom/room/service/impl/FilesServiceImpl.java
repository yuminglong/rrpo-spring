package com.jiebao.platfrom.room.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.room.dao.FilesMapper;
import com.jiebao.platfrom.room.domain.Files;
import com.jiebao.platfrom.room.service.IFilesService;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author qta
 * @since 2020-07-20
 */
@Service
public class FilesServiceImpl extends ServiceImpl<FilesMapper, Files> implements IFilesService {

    @Override
    public JiebaoResponse saveList(List<String> list) {
        List<Files> fileList = new ArrayList<>();
        for (String url : list
        ) {
            Files files = new Files();
            files.setUrl(url);
            fileList.add(files);
        }
        return new JiebaoResponse().data(fileList).message(saveBatch(fileList) ? "操作成功" : "操作失败");
    }
}
