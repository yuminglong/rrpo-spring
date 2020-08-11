package com.jiebao.platfrom.attendance.dao;

import com.jiebao.platfrom.attendance.daomain.Rule;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author qta
 * @since 2020-08-07
 */
@Repository
public interface RuleMapper extends BaseMapper<Rule> {
    @Update("update attendance_rule set using=0 where rule_id!=#{ruleId}")
    int updateByRuleId(String ruleId);//将其余规则  变为不启用

    @Select("select * from attendance_rule where using=1")
    Rule getRuleByUsing();  //得到启用的考勤规则
}
