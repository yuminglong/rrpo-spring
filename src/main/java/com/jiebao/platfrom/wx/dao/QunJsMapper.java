package com.jiebao.platfrom.wx.dao;

import com.jiebao.platfrom.wx.domain.QunJs;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author qta
 * @since 2020-08-31
 */
public interface QunJsMapper extends BaseMapper<QunJs> {
    @Select("select 1 from wx_qun_js where wx_id=#{wxId} limit 1")
    Integer judge(String wxId);
}
