package com.jiebao.platfrom.wx.service;

import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.domain.QueryRequest;
import com.jiebao.platfrom.wx.domain.Qun;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author qta
 * @since 2020-08-20
 */
public interface IQunService extends IService<Qun> {
    JiebaoResponse pageList(QueryRequest queryRequest, String name, String userName, Integer status);

//    JiebaoResponse updateStatus(String qunId);
//
//    JiebaoResponse up(String qunId);

    JiebaoResponse addOrUpdate(Qun qun);

    JiebaoResponse importQun(String content);

    void exPort(HttpServletResponse response, String[] deptId, String workName);//合格群导出

    public static void main(String[] args) {
        int[] nums = {1, 0, -1, 0, -2, 2};

    }
}
