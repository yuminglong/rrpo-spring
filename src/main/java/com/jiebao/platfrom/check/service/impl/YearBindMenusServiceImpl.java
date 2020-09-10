package com.jiebao.platfrom.check.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jiebao.platfrom.check.domain.YearBindMenus;
import com.jiebao.platfrom.check.dao.YearBindMenusMapper;
import com.jiebao.platfrom.check.service.IYearBindMenusService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author qta
 * @since 2020-09-03
 */
@Service
public class YearBindMenusServiceImpl extends ServiceImpl<YearBindMenusMapper, YearBindMenus> implements IYearBindMenusService {

    @Override
    public JiebaoResponse add(String yearID, String[] menusId) {
        JiebaoResponse jiebaoResponse = new JiebaoResponse();
        Integer integer = this.baseMapper.deleteByYearId(yearID);
        List<YearBindMenus> list = new ArrayList<>();
        for (String mId : menusId) {
            YearBindMenus yearBindMenus = new YearBindMenus();
            yearBindMenus.setYearId(yearID);
            yearBindMenus.setMenusId(mId);
            list.add(yearBindMenus);
        }
        jiebaoResponse = super.saveBatch(list) ? jiebaoResponse.okMessage("操作成功") : jiebaoResponse.failMessage("操作失败");
        return jiebaoResponse;
    }


    @Override
    public JiebaoResponse delete(String[] ids) {
        JiebaoResponse jiebaoResponse = new JiebaoResponse();
        jiebaoResponse = removeByIds(Arrays.asList(ids)) ? jiebaoResponse.okMessage("删除成功") : jiebaoResponse.failMessage("删除失败");
        return jiebaoResponse;
    }

    @Override
    public JiebaoResponse list(String yearId) {
        QueryWrapper<YearBindMenus> queryWrapper = new QueryWrapper<>();
        if (yearId != null) {
            queryWrapper.eq("year_id", yearId);
        }
        return new JiebaoResponse().data(list(queryWrapper)).message("查询成功");
    }


}
