package com.jiebao.platfrom.room.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.room.domain.Files;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author qta
 * @since 2020-07-20
 */
public interface IFilesService extends IService<Files> {

    JiebaoResponse saveList(List<String> list,Integer state);  //state 是否 可以公开

}
