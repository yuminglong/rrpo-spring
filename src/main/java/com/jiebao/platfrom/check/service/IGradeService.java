package com.jiebao.platfrom.check.service;

import com.jiebao.platfrom.check.domain.Grade;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import io.swagger.models.auth.In;

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
    JiebaoResponse addGrade(String menusId, Integer number, String yearDate, String deptId);    //扣分绑定

    JiebaoResponse commit(String yearDate, String deptId);//最后提交  分数统计生成表

    JiebaoResponse selectByUserIdOrDateYear(String dateYear, String DeptId);

    JiebaoResponse putZz(String gradeId, Integer type, String id);//上传佐证操作

}
