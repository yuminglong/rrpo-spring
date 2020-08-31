package com.jiebao.platfrom.check.service;

import com.jiebao.platfrom.check.domain.Grade;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import io.swagger.models.auth.In;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author qta
 * @since 2020-07-28
 */
public interface IGradeService extends IService<Grade> {
    JiebaoResponse addGrade(String gradeId, Double number, Double fpNumber, String message, String fpMessage);    //扣分绑定

    JiebaoResponse commit(String yearDate, String deptId,Integer status);//最后提交  分数统计生成表

    JiebaoResponse selectByUserIdOrDateYear(String dateYear, String DeptId);  //查询对应考试情况

    JiebaoResponse putZz(String yearDate, String deptId, String menusId, String[] ids, String[] xXhd, String[] ySyj, String[] tZgg, String[] gGxx);//上传佐证操作

    JiebaoResponse checkStatus(String gradeId, String[] zzId, String[] fileId, Integer status);//审核 考核项是否存在问题


}
