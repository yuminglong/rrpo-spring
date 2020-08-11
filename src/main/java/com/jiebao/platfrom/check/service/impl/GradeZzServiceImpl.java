package com.jiebao.platfrom.check.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jiebao.platfrom.check.domain.Grade;
import com.jiebao.platfrom.check.domain.GradeZz;
import com.jiebao.platfrom.check.dao.GradeZzMapper;
import com.jiebao.platfrom.check.service.IGradeService;
import com.jiebao.platfrom.check.service.IGradeZzService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author qta
 * @since 2020-08-11
 */
@Service
public class GradeZzServiceImpl extends ServiceImpl<GradeZzMapper, GradeZz> implements IGradeZzService {
    @Autowired
    IGradeService gradeService;

    @Override
    public JiebaoResponse list(String gradeId, String yearDate, String deptId, String menusId) {
        QueryWrapper<GradeZz> queryWrapper = new QueryWrapper<>();
        if (gradeId == null) {
            if (yearDate == null || deptId == null || menusId == null) {
                return new JiebaoResponse().message("信息不能为空");
            }
            QueryWrapper<Grade> queryWrapper1 = new QueryWrapper<>();  //考核 关联部分
            queryWrapper1.eq("year_date", yearDate);
            queryWrapper1.eq("dept_id", deptId);
            queryWrapper1.eq("check_id", menusId);
            Grade grade = gradeService.getOne(queryWrapper1);
            gradeId = grade.getGradeId();
        }
        queryWrapper.eq("grade_id", gradeId);
        return new JiebaoResponse().data(list(queryWrapper)).message("操作成功");
    }
}
