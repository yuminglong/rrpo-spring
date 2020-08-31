package com.jiebao.platfrom.check.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jiebao.platfrom.check.dao.GradeZzMapper;
import com.jiebao.platfrom.check.dao.MenusMapper;
import com.jiebao.platfrom.check.dao.MenusYearMapper;
import com.jiebao.platfrom.check.domain.*;
import com.jiebao.platfrom.check.dao.GradeMapper;
import com.jiebao.platfrom.check.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
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

    @Override
    public JiebaoResponse addGrade(String gradeId, Double number, Double fpNumber, String message, String fpMessage) {  //menusId  既是 扣分项id
        QueryWrapper<Grade> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("grade_id", gradeId);//年份
        Grade grade = getOne(queryWrapper);
        boolean numberIf = number == null ? false : true;
        boolean fpNumberIf = fpNumber == null ? false : true;
        boolean messageIf = message == null ? false : true;
        boolean fpMessageIf = fpMessage == null ? false : true;
        if (grade == null) {
            grade = new Grade();
        }
        if (numberIf)
            grade.setNum(number);
        if (fpNumberIf)
            grade.setFpNum(fpNumber);
        if (messageIf)
            grade.setMessage(message);
        if (fpMessageIf)
            grade.setFpMessage(fpMessage);
        return new JiebaoResponse().message(super.saveOrUpdate(grade) ? "操作成功" : "操作失败").data(grade);
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
            List<Grade> grades = this.baseMapper.queryList(yearId, deptId, menus.getStandardId());
            if (grades == null) {  //此分组无数据则结束
                break;
            }
            if (menus.getName().equals("基础工作")) {
                for (Grade grade : grades) {
                    if (grade.getNum() == null || grade.getNum() > 0) {  //如果大于0 证明为加分项
                        JCJF += grade.getNum() == null ? 0 : grade.getNum();
                        fpJcJf += grade.getFpNum() == null ? 0 : grade.getFpNum();
                    } else {  //反之 为扣分项
                        JCKF += grade.getNum() == null ? 0 : grade.getNum();
                        fpJcKf += grade.getFpNum() == null ? 0 : grade.getFpNum();
                    }
                }
            } else if (menus.getName().equals("工作效果")) {
                for (Grade grade : grades) {
                    SGKF += grade.getNum() == null ? 0 : grade.getNum();
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
            yearZu.setList(Collections.singletonList(this.baseMapper.queryList(yearId, DeptId, menus.getStandardId())));
            list1.add(yearZu);
        }
        return new JiebaoResponse().data(list1).message("操作成功");
    }

    @Override
    public JiebaoResponse putZz(String yearId, String deptId, String menusId, String[] ids, String[] xXhd, String[] ySyj, String[] tZgg, String[] gGxx) {//上传新的佐证材料需要修改状态
        List<File> list = new ArrayList<>();
        List<GradeZz> gradeZzList = new ArrayList<>();
        QueryWrapper<Grade> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("check_id", menusId);//对应的扣分项
        queryWrapper.eq("year_id", yearId);//年份
        queryWrapper.eq("dept_id", deptId);
        Grade grade = getOne(queryWrapper);  //对应考核项具体扣分情况
        if (grade == null) {
            grade = new Grade();
        }
        String gradeId = grade.getGradeId();
        System.out.println(gradeId);
        if (ids != null) {
            for (String fileId : ids  //
            ) {
                File file = fileService.getById(fileId);
                file.setRefId(gradeId);
                file.setRefType("4");
                list.add(file);
            }
        }

        if (xXhd != null) {
            for (String xXhdId : xXhd
            ) {
                if (isCuiZai(gradeId, xXhdId)) {
                    break;
                }
                GradeZz gradeZz = new GradeZz();
                gradeZz.setType(1);
                gradeZz.setGradeId(gradeId);
                gradeZz.setZzId(xXhdId);
                gradeZzList.add(gradeZz);
            }
        }

        if (ySyj != null) {
            for (String ySyjId : ySyj
            ) {
                System.out.println(ySyjId);
                if (isCuiZai(gradeId, ySyjId)) {
                    break;
                }
                GradeZz gradeZz = new GradeZz();
                gradeZz.setType(2);
                gradeZz.setGradeId(gradeId);
                gradeZz.setZzId(ySyjId);
                gradeZzList.add(gradeZz);
            }
        }
        if (tZgg != null) {
            for (String tZggId : tZgg
            ) {
                if (isCuiZai(gradeId, tZggId)) {
                    break;
                }
                GradeZz gradeZz = new GradeZz();
                gradeZz.setType(3);
                gradeZz.setGradeId(gradeId);
                gradeZz.setZzId(tZggId);
                gradeZzList.add(gradeZz);
            }
        }

        if (gGxx != null) {
            for (String gGxxId : gGxx
            ) {
                if (isCuiZai(gradeId, gGxxId)) {
                    break;
                }
                GradeZz gradeZz = new GradeZz();
                gradeZz.setType(4);
                gradeZz.setGradeId(gradeId);
                gradeZz.setZzId(gGxxId);
                gradeZzList.add(gradeZz);
            }
        }
        boolean a = true, b = true;
        if (list.size() != 0) {
            a = fileService.updateBatchById(list);
        }
        if (gradeZzList.size() != 0) {
            b = gradeZzService.saveBatch(gradeZzList);
        }
        return new JiebaoResponse().message(a && b ? "添加成功" : "添加失败").data(gradeId);
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
