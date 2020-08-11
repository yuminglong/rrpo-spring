package com.jiebao.platfrom.attendance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiebao.platfrom.attendance.daomain.Rule;
import com.jiebao.platfrom.attendance.dao.RuleMapper;
import com.jiebao.platfrom.attendance.service.IRuleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.domain.QueryRequest;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author qta
 * @since 2020-08-07
 */
@Service
public class RuleServiceImpl extends ServiceImpl<RuleMapper, Rule> implements IRuleService {

    @Override
    public JiebaoResponse addOrUpdate(Rule rule) {
        QueryWrapper<Rule> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", rule.getName());
        Rule one = getOne(queryWrapper);
        if (one != null) {
            return new JiebaoResponse().message("考勤规则名字重复");
        }
        queryWrapper = new QueryWrapper<>();
        if (rule.getRuleId() != null) {
            if (rule.getUsing() == 1) {  //此规则启用   别的都禁用
                int i = this.baseMapper.updateByRuleId(rule.getRuleId());
                if (i == 0) {
                    return new JiebaoResponse().message("操作失败");
                }
            }
        }
        return new JiebaoResponse().message(saveOrUpdate(rule) ? "操作成功" : "操作失败");
    }

    @Override
    public JiebaoResponse pageList(QueryRequest queryRequest, String name) {
        QueryWrapper<Rule> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", name);
        Page<Rule> page = new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize());
        return new JiebaoResponse().data(page(page, queryWrapper)).message("查询成功");
    }
}
