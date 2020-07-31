package com.jiebao.platfrom.check.service;

import com.jiebao.platfrom.check.domain.Grade;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jiebao.platfrom.common.domain.JiebaoResponse;

import java.util.Date;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author qta
 * @since 2020-07-28
 */
public interface IGradeService extends IService<Grade> {
    JiebaoResponse addGrade(String menusId, double number);

    JiebaoResponse commit();//最后提交  分数统计生成表

    JiebaoResponse selectByUserIdOrDateYear(String dateYear, String userId);

}
