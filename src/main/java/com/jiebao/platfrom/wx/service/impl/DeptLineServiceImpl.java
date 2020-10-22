package com.jiebao.platfrom.wx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.wx.entity.DeptLine;
import com.jiebao.platfrom.wx.mapper.DeptLineMapper;
import com.jiebao.platfrom.wx.service.IDeptLineService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author qta
 * @since 2020-10-21
 */
@Service
public class DeptLineServiceImpl extends ServiceImpl<DeptLineMapper, DeptLine> implements IDeptLineService {

    @Override
    public JiebaoResponse getLine(String deptId) {
        QueryWrapper<DeptLine> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dept_id", deptId);
        return new JiebaoResponse().data(list(queryWrapper)).okMessage("查询成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JiebaoResponse add(DeptLine deptLine) {
        JiebaoResponse jiebaoResponse = new JiebaoResponse();
        return save(deptLine) ? jiebaoResponse.okMessage("操作成功") : jiebaoResponse.failMessage("操作失败").data(deptLine);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public JiebaoResponse delete(String[] ids) {
        JiebaoResponse jiebaoResponse = new JiebaoResponse();
        return removeByIds(Arrays.asList(ids)) ? jiebaoResponse.okMessage("删除成功") : jiebaoResponse.failMessage("删除失败");
    }
}
