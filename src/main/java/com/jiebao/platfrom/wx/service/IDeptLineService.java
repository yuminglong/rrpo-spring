package com.jiebao.platfrom.wx.service;

import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.wx.domain.DeptLine;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author qta
 * @since 2020-09-10
 */
public interface IDeptLineService extends IService<DeptLine> {

    void setDeptLine(String deptId, String qunId);

    String getDownDeptId(String qunId, String deptId);//返回  传入部门的下级部门  全部  线性关系

}
