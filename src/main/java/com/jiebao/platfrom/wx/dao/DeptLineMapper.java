package com.jiebao.platfrom.wx.dao;

import com.jiebao.platfrom.wx.domain.DeptLine;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author qta
 * @since 2020-09-10
 */
public interface DeptLineMapper extends BaseMapper<DeptLine> {
    @Select("select 1 from wx_dept_line where qun_id=#{qunId} and dept_id=#{deptId}  limit 1")
    Integer exist(String qunId, String deptId);//  是否存在这条数据

    @Select("select max(number) from wx_dept_line where qun_id=#{qunId} ")
    Integer maxNumber(String qunId);//  最大节点

    @Select("select * from wx_dept_line where qun_id=#{qunId} and dept_id=#{deptId}")
    DeptLine selectDeptLine(String qunId, String deptId);//查找对应数据

    @Select("select * from wx_dept_line where qun_id=#{qunId} and number=#{number}")
    DeptLine selectDeptLine(String qunId, Integer number);//查找对应数据
}
