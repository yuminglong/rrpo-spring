package com.jiebao.platfrom.check.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jiebao.platfrom.check.dao.GradeZzMapper;
import com.jiebao.platfrom.check.domain.Grade;
import com.jiebao.platfrom.check.dao.GradeMapper;
import com.jiebao.platfrom.check.domain.GradeZz;
import com.jiebao.platfrom.check.domain.Menus;
import com.jiebao.platfrom.check.domain.Num;
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

    @Override
    public JiebaoResponse addGrade(String menusId, Double number, String yearId, String deptId, Double fpNumber, String message, String fpMessage) {  //menusId  既是 扣分项id
        QueryWrapper<Grade> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("check_id", menusId);//对应的扣分项
        queryWrapper.eq("year_id", yearId);//年份
        queryWrapper.eq("dept_id", deptId);
        Grade grade = getOne(queryWrapper);
        boolean numberIf = number == null ? false : true;
        boolean fpNumberIf = fpNumber == null ? false : true;
        if (grade != null) {
            if (numberIf)
                grade.setNum(number);
            if (fpNumberIf)
                grade.setFpNum(fpNumber);
            grade.setMessage(message);
            grade.setFpMessage(fpMessage);
        } else {
            grade = new Grade();
            if (numberIf)
                grade.setNum(number);
            grade.setCheckId(menusId);
            grade.setDeptId(deptId);
            grade.setYearId(yearId);
            if (fpNumberIf)
                grade.setFpNum(fpNumber);
            grade.setMessage(message);
            grade.setFpMessage(fpMessage);
        }
        return new JiebaoResponse().message(super.saveOrUpdate(grade) ? "操作成功" : "操作失败").data(grade);
    }

    @Override
    public JiebaoResponse commit(String yearId, String deptId) {   //  生成报表
        QueryWrapper<Grade> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(("dept_id"), deptId);
        queryWrapper.eq("year_id", yearId);
        List<Grade> list = list(queryWrapper);
        QueryWrapper<Menus> queryWrapperJC = new QueryWrapper<>();  //  基础工作
        queryWrapperJC.eq("content", "基础工作");
        Menus menusJc = menusService.getOne(queryWrapperJC);//基础工作父类s
        QueryWrapper<Menus> queryWrapperXG = new QueryWrapper<>();
        queryWrapperXG.eq("content", "工作效果");//效果
        Menus menusSX = menusService.getOne(queryWrapperXG);//工作失效父类
        double JCKF = 0;  //基础工作的扣分项
        double JCJF = 0;//基础工作加分项
        double SGKF = 0;//工作效果扣分项
        double fpJcKf = 0, fpJcJf = 0, fpSgK = 0;
        for (Grade grade : list) {
            Menus menus = menusService.getById(grade.getCheckId());
            if (menus.getParentId().equals(menusJc.getMenusId())) {  //此条数据对应   基础工作  扣分模块规则
                if (grade.getNum() > 0) {  //如果大于0 证明为加分项
                    JCJF += grade.getNum();
                    fpJcJf += grade.getFpNum();
                } else {  //反之 为扣分项
                    JCKF += grade.getNum();
                    fpJcKf += grade.getFpNum();

                }
            } else {
                SGKF += grade.getNum();
                fpSgK += grade.getNum();
            }
        }
        SGKF = 40 + SGKF;

        fpSgK = 40 + fpSgK;

        SGKF = SGKF < 0 ? 0 : SGKF;

        fpSgK = fpSgK < 0 ? 0 : fpSgK;

        JCKF = JCKF < -20 ? -20 : JCKF;  //扣分  20为限

        fpJcKf = fpJcKf < -20 ? -20 : fpJcKf;  //扣分  20为限

        if (JCJF > 40) {
            double dx = (JCJF - 40) / 15;  //溢出部分抵消扣分
            JCKF = JCKF + dx;  //抵消后的分数
            if (JCKF > 0) {
                JCKF = 0;
            }
            JCJF = 40;  //40分为限  超过40分  折价  15：1
        }

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
        num.setJcWork(JCKF + JCJF);
        num.setXgWork(SGKF);
        num.setNumber(20 + JCKF + JCJF + SGKF);
        num.setYearDate(yearService.getById(yearId).getYearDate());
        num.setFpJcWork(fpJcKf + fpJcJf);
        num.setFpXgWork(fpSgK);
        num.setFpNumber(20 + fpJcKf + fpJcJf + fpSgK);
        return new JiebaoResponse().message(numService.saveOrUpdate(num) ? "操作成功" : "操作失败");
    }

    @Override
    public JiebaoResponse selectByUserIdOrDateYear(String yearId, String DeptId) {  //必填 时间   组织id   年份
        QueryWrapper<Grade> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("year_id", yearId);
        queryWrapper.eq("dept_id", DeptId);
        List<Grade> list = list(queryWrapper);   //目标人  关联扣分项
        QueryWrapper<Menus> queryWrapperJC = new QueryWrapper<>();  //  基础工作
        queryWrapperJC.eq("content", "基础工作");
        Menus menusJc = menusService.getOne(queryWrapperJC);//基础工作父类
        QueryWrapper<Menus> queryWrapperXG = new QueryWrapper<>();
        queryWrapperXG.eq("content", "工作效果");//效果
        Menus menusSX = menusService.getOne(queryWrapperXG);//工作失效父类
        List<Menus> jcList = new ArrayList<>(); //储存基础map
        List<Menus> xgList = new ArrayList<>();   //储存工作实效map
        HashMap<String, List<Menus>> map = new HashMap<>();   //总map
        for (Grade grade : list) {
            Menus menus = menusService.getById(grade.getCheckId());
            menus.setGrade(grade);
            if (menus.getParentId().equals(menusJc.getMenusId())) {  //基础工作模块
                jcList.add(menus);
            } else {  //工作效果模块
                xgList.add(menus);
            }
        }
        map.put("JCgz", jcList);
        map.put("GZxg", xgList);
        return new JiebaoResponse().data(map).message("操作成功");
    }

    @Override
    public JiebaoResponse putZz(String yearId, String deptId, String menusId, String[] ids, String[] xXhd, String[] ySyj, String[] tZgg, String[] gGxx) {
        List<File> list = new ArrayList<>();
        List<GradeZz> gradeZzList = new ArrayList<>();
        QueryWrapper<Grade> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("check_id", menusId);//对应的扣分项
        queryWrapper.eq("year_id", yearId);//年份
        queryWrapper.eq("dept_id", deptId);
        Grade grade = getOne(queryWrapper);  //对应考核项具体扣分情况
        System.out.println(grade);
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
        new Thread() {
            public void run() {
                bjGradeId(gradeId, status);
            }
        }.start();
        return jiebaoResponse;
    }

    private void bjGradeId(String gradeId, Integer status) {  //标记扣分项   存在疑点便不可以 修改
        Grade grade = getById(gradeId);
        Integer i = gradeMapper.gradeZzExistStatusNull(gradeId);  //非自定义文件
        Integer i1 = gradeMapper.fileExistStatusNull(gradeId);//自定义文件
        if (i == null && i1 == null) {  //全部审核完才能
            grade.setAudit(0);
        } else {
            grade.setAudit(1);
        }
        Integer i2 = gradeMapper.gradeExIs(grade.getYearId(), grade.getDeptId());  //是否存在有未审核的扣分项
        int audit = 1;
        if (i2 == null) {  //全部审核完
            audit = 0;
        }
        gradeMapper.updateNum(grade.getYearId(), grade.getDeptId(), audit);
        if (status == 0) {// 解除疑点操作  则需要遍历   附属  佐证  是否 还存在 疑点
            Integer exIsGrade = gradeMapper.gradeZzExistStatus(gradeId);//  本扣分项 包含的所有的  非自定义扣分项
            Integer exISFile = gradeMapper.fileExistStatus(gradeId);//自定义文件的 绑定材料   不合格的全部查出来
            if (exIsGrade != null || exISFile != null) {  //只要附属材料有一个疑点 存在 就不能改为合格
                return;
            }
        }
        grade.setStatus(status);
        updateById(grade);
        QueryWrapper<Num> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("year_id", grade.getYearId());
        queryWrapper.eq("dept_id", grade.getDeptId());
        Num num = numService.getOne(queryWrapper);  //对应的年份 统计总表
        numService.updateById(num.setStatus(status));
        //判断  是否审核完
    }
}
