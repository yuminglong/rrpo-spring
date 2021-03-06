package com.jiebao.platfrom.wx.service;

import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.domain.QueryRequest;
import com.jiebao.platfrom.wx.domain.UserI;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author qta
 * @since 2020-08-20
 */
public interface IUserIService extends IService<UserI> {
    JiebaoResponse deleteS(String[] wxUserIdS,String qunId);
    JiebaoResponse list(QueryRequest queryRequest,String deptId, String name,String wxQunId);
}
