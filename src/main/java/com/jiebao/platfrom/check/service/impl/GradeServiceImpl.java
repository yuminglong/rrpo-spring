package com.jiebao.platfrom.check.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jiebao.platfrom.check.dao.GradeZzMapper;
import com.jiebao.platfrom.check.dao.MenusMapper;
import com.jiebao.platfrom.check.dao.MenusYearMapper;
import com.jiebao.platfrom.check.domain.*;
import com.jiebao.platfrom.check.dao.GradeMapper;
import com.jiebao.platfrom.check.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiebao.platfrom.common.authentication.JWTUtil;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.railway.service.AsYearService;
import com.jiebao.platfrom.system.dao.UserMapper;
import com.jiebao.platfrom.system.domain.File;
import com.jiebao.platfrom.system.domain.User;
import com.jiebao.platfrom.system.service.FileService;
import io.swagger.models.auth.In;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.SimpleFormatter;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author qta
 * @since 2020-07-28
 */
@Service
public class GradeServiceImpl extends ServiceImpl<GradeMapper, Grade> implements IGradeService {
    @Autowired
    IMenusService menusService;
    @Autowired
    MenusMapper menusMapper;
    @Autowired
    INumService numService;
    @Autowired
    IGradeZzService gradeZzService;
    @Autowired
    FileService fileService;
    @Autowired
    GradeZzMapper gradeZzMapper;
    @Autowired
    IYearService yearService;
    @Autowired
    GradeMapper gradeMapper;
    @Autowired
    MenusYearMapper menusYearMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    AsYearService asYearService;

    @Override
    public JiebaoResponse addGrade(String gradeId, Double number, String message, Integer type) {  //menusId  既是 扣分项id   type代表三组不同的参数
        JiebaoResponse jiebaoResponse = new JiebaoResponse();
        Grade grade = getById(gradeId);
        Num num = numService.selectByYearAndDept(grade.getYearId(), grade.getDeptId());
        if (num == null) {
            if (type == 2 || type == 3) {
                return jiebaoResponse.failMessage("此状态不可操作");
            }
        } else {
            if (type == 1) {
                return jiebaoResponse.failMessage("此状态不可操作");
            }
            if (!(num.getStatus() == 1)) {
                if (type == 1 || type == 2) {
                    return jiebaoResponse.failMessage("此状态不可操作");
                }
            }
        }
        if (!(number == null)) {
            if (type == 1)
                grade.setNum(number);
            if (type == 2)
                grade.setNum2(number);
            if (type == 3)
                grade.setFpNum(number);
        }
        if (!(message == null)) {
            if (type == 1)
                grade.setMessage(message);
            if (type == 2)
                grade.setMessage2(message);
            if (type == 3)
                grade.setFpMessage(message);
        }
        double math;
        if (type == 3 && number != null) {

            math = grade.getNum2() == null ? grade.getNum() : grade.getNum2();  //分数    取  第一次自评还是第三次自评
            if (number == math) {
                grade.setStatus(1);
            } else {
                grade.setStatus(0);
            }
        }
        if (type == 2 && number != null) {
            if (number == grade.getFpNum()) {
                grade.setStatus(1);
            } else {
                grade.setStatus(0);
            }
        }
        jiebaoResponse = updateById(grade) ? jiebaoResponse.okMessage("操作成功") : jiebaoResponse.failMessage("操作失败");
        return jiebaoResponse;
    }


    @Override
    public JiebaoResponse commit(String yearId, String deptId, Integer status) {   //  生成报表
        if (this.baseMapper.exist(yearId, deptId) == null) {
            return null;
        }
        double JCKF = 0;  //基础工作的扣分项
        double JCJF = 0;//基础工作加分项
        double SGKF = 0;//工作效果扣分项
        double fpJcKf = 0, fpJcJf = 0, fpSgK = 0;
        List<Menus> menusList = menusService.list();
        for (Menus menus : menusList
        ) {
            QueryWrapper<Grade> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("year_id", yearId);
            queryWrapper.eq("dept_id", deptId);
            queryWrapper.eq("parent_id", menus.getStandardId());
            List<Grade> grades = this.baseMapper.queryList(queryWrapper);
            if (grades == null) {  //此分组无数据则结束
                break;
            }
            if (menus.getName().equals("基础工作")) {
                for (Grade grade : grades) {
                    if (grade.getNum() == null || grade.getNum() > 0) {  //如果大于0 证明为加分项
                        if (grade.getNum2() != null) {
                            JCJF += grade.getNum2();
                        } else {
                            JCJF += grade.getNum() == null ? 0 : grade.getNum();
                        }
                        fpJcJf += grade.getFpNum() == null ? 0 : grade.getFpNum();
                    } else {  //反之 为扣分项
                        if (grade.getNum2() != null) {
                            JCKF += grade.getNum2();
                        } else {
                            JCKF += grade.getNum() == null ? 0 : grade.getNum();
                        }
                        fpJcKf += grade.getFpNum() == null ? 0 : grade.getFpNum();
                    }
                }
            } else if (menus.getName().equals("工作效果")) {
                for (Grade grade : grades) {
                    if (grade.getNum2() != null) {
                        SGKF += grade.getNum2();
                    } else {
                        SGKF += grade.getNum() == null ? 0 : grade.getNum();
                    }
                    fpSgK += grade.getFpNum() == null ? 0 : grade.getFpNum();
                }
            }
        }
        SGKF = 40 + SGKF;
        fpSgK = 40 + fpSgK;//
        SGKF = SGKF < 0 ? 0 : SGKF;
        SGKF = SGKF >= 40 ? 40 : SGKF;
        fpSgK = fpSgK < 0 ? 0 : fpSgK;//
        fpSgK = fpSgK >= 40 ? 40 : fpSgK;//
        JCKF = JCKF < -20 ? -20 : JCKF;  //扣分  20为限
        JCKF = JCKF >= 0 ? 0 : JCKF;  //扣分  20为限
        fpJcKf = fpJcKf < -20 ? -20 : fpJcKf;  //扣分  20为限//
        fpJcKf = fpJcKf >= 0 ? 0 : fpJcKf;  //扣分  20为限
        JCJF = JCJF <= 0 ? 0 : JCJF;
        if (JCJF > 40) {
            double dx = (JCJF - 40) / 15;  //溢出部分抵消扣分
            JCKF = JCKF + dx;  //抵消后的分数
            if (JCKF > 0) {
                JCKF = 0;
            }
            JCJF = 40;  //40分为限  超过40分  折价  15：1
        }
        fpJcJf = fpJcJf <= 0 ? 0 : fpJcJf;
        if (fpJcJf > 40) {
            double dx = (fpJcJf - 40) / 15;  //溢出部分抵消扣分
            fpJcKf = JCKF + dx;  //抵消后的分数
            if (fpJcKf > 0) {
                fpJcKf = 0;
            }
            fpJcJf = 40;  //40分为限  超过40分  折价  15：1
        }
        QueryWrapper<Num> numQueryWrapper = new QueryWrapper<>();
        numQueryWrapper.eq("year_id", yearId);
        numQueryWrapper.eq("dept_id", deptId);
        Num num = numService.getOne(numQueryWrapper);  //年度考核表储存对象
        if (num == null) {
            num = new Num();  //为空创造对象
        }
        num.setDeptId(deptId);
        num.setYearId(yearId);
        num.setJcWork(JCKF + JCJF + 20);
        num.setXgWork(SGKF);
        num.setNumber(20 + JCKF + JCJF + SGKF);
        num.setYearDate(yearService.getById(yearId).getYearDate());
        num.setFpJcWork(fpJcKf + fpJcJf + 20);
        num.setFpXgWork(fpSgK);
        num.setFpNumber(20 + fpJcKf + fpJcJf + fpSgK);
        num.setStatus(status);
        return new JiebaoResponse().message(numService.saveOrUpdate(num) ? "操作成功" : "操作失败");
    }

    @Override
    public JiebaoResponse selectByUserIdOrDateYear(String yearId, String DeptId) {  //必填 时间   组织id   年份
        List<YearZu> list1 = new ArrayList<>();  //储存
        List<Menus> list = menusService.list();
        for (Menus menus : list
        ) {
            YearZu yearZu = new YearZu();
            yearZu.setId(menus.getStandardId());
            yearZu.setNum(menus.getNum());
            yearZu.setName(menus.getName());
            QueryWrapper<Grade> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("year_id", yearId);
            queryWrapper.eq("dept_id", DeptId);
            queryWrapper.eq("parent_id", menus.getStandardId());
            yearZu.setList(this.baseMapper.queryList(queryWrapper));
            list1.add(yearZu);
        }
        return new JiebaoResponse().data(list1).message("操作成功");
    }

    @Override
    public JiebaoResponse putZz(String gradeId, String[] ids) {//上传新的佐证材料需要修改状态
        return asYearService.addAsYear(gradeId, ids);
    }

    private boolean isCuiZai(String gradeId, String ZzId) {
        if (gradeZzMapper.ExIstGradeZz(gradeId, ZzId) == null)//存在返回true
            return false;
        return true;
    } //判断佐证是否已经和 此对象绑定好

    @Override
    public JiebaoResponse checkStatus(String gradeId, String[] zzId, String[] fileId, Integer status) {
        JiebaoResponse jiebaoResponse = new JiebaoResponse();
        if (gradeId == null) {
            return jiebaoResponse.failMessage("请提交必要参数");
        }//给扣分项  给  状态  是否有疑点‘
        jiebaoResponse = this.baseMapper.updateStatus(status, gradeId) == 1 ? jiebaoResponse.okMessage("标记成功") : jiebaoResponse.failMessage("标记失败");
        if (zzId != null) {   //非自定义佐证材料
            QueryWrapper<GradeZz> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("grade_zz_id", Arrays.asList(zzId));
            int i = gradeMapper.updateGradeZZ(status, queryWrapper);
            jiebaoResponse = i == 1 ? jiebaoResponse.okMessage("年度审核材料佐证疑点标记成功") : jiebaoResponse.failMessage("年度审核材料佐证疑点标记失败");
        }
        if (fileId != null) {  //自定义佐证材料
            QueryWrapper<File> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("file_id", Arrays.asList(fileId));
            int i = gradeMapper.updateFile(status, queryWrapper);
            jiebaoResponse = i == 1 ? jiebaoResponse.okMessage("自定义年度审核材料疑点标记成功") : jiebaoResponse.failMessage("自定义年度审核材料疑点标记失败");
        }
        return jiebaoResponse;
    }


}
