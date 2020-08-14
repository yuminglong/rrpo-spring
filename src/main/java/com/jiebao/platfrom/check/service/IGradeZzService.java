package com.jiebao.platfrom.check.service;

import com.jiebao.platfrom.check.domain.GradeZz;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jiebao.platfrom.common.domain.JiebaoResponse;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author qta
 * @since 2020-08-11
 */
public interface IGradeZzService extends IService<GradeZz> {
    JiebaoResponse list(String gradeId, String yearDate, String deptId, String menusId,Integer type);

    JiebaoResponse getData(Integer type, Integer status);

    JiebaoResponse deleteByGradeIdAndZzId(String[] list,String gradeId);
}
