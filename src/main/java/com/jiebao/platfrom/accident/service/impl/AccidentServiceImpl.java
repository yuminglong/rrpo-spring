package com.jiebao.platfrom.accident.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiebao.platfrom.accident.daomain.Accident;
import com.jiebao.platfrom.accident.dao.AccidentMapper;
import com.jiebao.platfrom.accident.service.IAccidentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.domain.QueryRequest;
import com.jiebao.platfrom.system.domain.Dept;
import com.jiebao.platfrom.system.service.DeptService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author qta
 * @since 2020-08-04
 */
@Service
public class AccidentServiceImpl extends ServiceImpl<AccidentMapper, Accident> implements IAccidentService {
    @Autowired
    DeptService deptService;

    @Override
    public JiebaoResponse list(QueryRequest queryRequest, String cityCsId, String cityQxId, String startDate, String endDate) {
        QueryWrapper<Accident> queryWrapper = new QueryWrapper<>();
        if (cityQxId != null) {
            queryWrapper.eq("city_qx_id", cityQxId);
        } else if (cityCsId != null) {
            queryWrapper.eq("city_cs_id", cityCsId);
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (startDate != null) {
            queryWrapper.ge("date", startDate);    //不能小于此时间
        }
        if (endDate != null) {
            queryWrapper.le("date", endDate);//不能大于此时间}
        }
        Page<Accident> page = new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize());
        return new JiebaoResponse().data(this.baseMapper.ListPage(page, queryWrapper)).message("查询成功");
    }

    @Override
    public JiebaoResponse map(String startDate, String endDate, Integer status) {
        if (status == null) {
            return new JiebaoResponse().failMessage("请选择类型");
        }
        List<Dept> childrenList = deptService.getChildrenList("0");  //市州级别  详情
        return new JiebaoResponse().data(fz(childrenList, startDate, endDate, status)).okMessage("查询成功");
    }

    private Map fz(List<Dept> deptList, String StartDate, String endDate, Integer status) {  //事故性质  数据
        HashMap<String, List<String>> map = new HashMap<>();
        List<String> city = new ArrayList<>();
        List<String> list = null;
        if (status == 1) {
            list = this.baseMapper.sgXz();  //事故性质
        } else if (status == 2) {
            list = this.baseMapper.age(); //年龄
        } else if (status == 3) {
            list = this.baseMapper.conditions();//事故情形类别
        } else if (status == 4) {
            list = this.baseMapper.identity();//身份
        }
        boolean bool = true;
        for (Dept dept :
                deptList) {
            city.add(dept.getDeptName());
            int b = 1;//记录存储第几个数组
            for (String str : list
            ) {
                QueryWrapper<Accident> queryWrapper = new QueryWrapper();
                if (StartDate != null) {
                    queryWrapper.ge("date", StartDate);
                }
                if (endDate != null) {
                    queryWrapper.le("date", endDate);
                }
                queryWrapper.eq("city_cs_id", dept.getDeptId());
                queryWrapper.eq("nature", str);
                if (bool) {
                    map.put(str + b, new ArrayList<String>());
                }
                map.get(str + b).add(this.baseMapper.count(queryWrapper).toString());
                ++b;
            }
            bool = false; //只需第一次执行
        }
        map.put("city", city);
        return map;
    }


}
