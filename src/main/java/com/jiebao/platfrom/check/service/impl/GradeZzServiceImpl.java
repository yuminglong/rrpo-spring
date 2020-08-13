package com.jiebao.platfrom.check.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jiebao.platfrom.check.domain.Grade;
import com.jiebao.platfrom.check.domain.GradeZz;
import com.jiebao.platfrom.check.dao.GradeZzMapper;
import com.jiebao.platfrom.check.service.IGradeService;
import com.jiebao.platfrom.check.service.IGradeZzService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.railway.domain.Exchange;
import com.jiebao.platfrom.railway.domain.Inform;
import com.jiebao.platfrom.railway.domain.Prize;
import com.jiebao.platfrom.railway.domain.PublicFile;
import com.jiebao.platfrom.railway.service.ExchangeService;
import com.jiebao.platfrom.railway.service.InformService;
import com.jiebao.platfrom.railway.service.PrizeService;
import com.jiebao.platfrom.railway.service.PublicFileService;
import com.jiebao.platfrom.system.dao.UserMapper;
import com.jiebao.platfrom.system.domain.User;
import com.jiebao.platfrom.system.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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
    @Autowired
    ExchangeService exchangeService;
    @Autowired
    UserService userService;
    @Autowired
    UserMapper userMapper;
    @Autowired
    PrizeService prizeService;
    @Autowired
    InformService informService;
    @Autowired
    PublicFileService publicFileService;
    @Autowired
    GradeZzMapper gradeZzMapper;

    @Override
    public JiebaoResponse deleteByGradeIdAndZzId(String[] list, String gradeId) {
        QueryWrapper<GradeZz> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("zz_id", Arrays.asList(list));
        queryWrapper.eq("grade_id", gradeId);
        return new JiebaoResponse().message(remove(queryWrapper) ? "解除绑定成功" : "解除绑定失败");
    }

    @Override
    public JiebaoResponse list(String gradeId, String yearDate, String deptId, String menusId, Integer type) {
        if (gradeId == null) {
            if (yearDate == null || deptId == null || menusId == null) {
                return new JiebaoResponse().message("信息不能为空");
            }
            QueryWrapper<Grade> queryWrapper1 = new QueryWrapper<>();  //考核 关联部分
            queryWrapper1.eq("year_date", yearDate);
            queryWrapper1.eq("dept_id", deptId);
            queryWrapper1.eq("check_id", menusId);
            Grade grade = gradeService.getOne(queryWrapper1);
            if (grade == null) {
                return new JiebaoResponse().message("无对象");
            }
            gradeId = grade.getGradeId();
        }
        List<String> zzId = gradeZzMapper.getZzId(type, gradeId); //佐证的id
        if (zzId.size() == 0) {
            return new JiebaoResponse().message("无对象");
        }

        if (type == 1)  //信息互递
            return new JiebaoResponse().data(exchangeService.getBaseMapper().selectBatchIds(zzId)).message("操作成功");
        ;
        if (type == 2)  //一事一奖
            return new JiebaoResponse().data(prizeService.getBaseMapper().selectBatchIds(zzId)).message("操作成功");
        ;
        if (type == 3) //通知公告
            return new JiebaoResponse().data(informService.getBaseMapper().selectBatchIds(zzId)).message("操作成功");
        ;
        if (type == 4)//公共信息
            return new JiebaoResponse().data(publicFileService.getBaseMapper().selectBatchIds(zzId)).message("操作成功");
        ;
        return null;
    }

    @Override
    public JiebaoResponse getData(Integer type, Integer status) {
        String userName = (String) SecurityUtils.getSubject().getPrincipal();
        List<String> userIdByDepts = userMapper.getUserIdByDepts(userMapper.getUser(userName));//当前市州相关人员
        if (type == 1)
            return new JiebaoResponse().data(ExchangeList(userIdByDepts, status));
        if (type == 2)
            return new JiebaoResponse().data(PrizeList(userIdByDepts, status));
        if (type == 3)
            return new JiebaoResponse().data(InformList(userIdByDepts, status));
        if (type == 4)
            return new JiebaoResponse().data(PublicFileList(userIdByDepts, status));
        return null;
    }


    private List<Exchange> ExchangeList(List<String> Username, Integer status) { //去信息互递拿值
        QueryWrapper<Exchange> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("create_user", Username);
        queryWrapper.eq("is_check", status);
        return exchangeService.list(queryWrapper);
    }

    private List<Prize> PrizeList(List<String> Username, Integer status) { //一事一奖
        QueryWrapper<Prize> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("create_user", Username);
        queryWrapper.eq("is_check", status);
        return prizeService.list(queryWrapper);
    }

    private List<Inform> InformList(List<String> Username, Integer status) { //通知公告
        QueryWrapper<Inform> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("create_user", Username);
        queryWrapper.eq("is_check", status);
        return informService.list(queryWrapper);
    }

    private List<PublicFile> PublicFileList(List<String> Username, Integer status) { //公共信息  //未写好
        QueryWrapper<PublicFile> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("create_user", Username);
        queryWrapper.eq("is_check", status);
        return publicFileService.list(queryWrapper);
    }


}
