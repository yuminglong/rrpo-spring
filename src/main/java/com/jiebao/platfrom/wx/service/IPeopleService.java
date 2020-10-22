package com.jiebao.platfrom.wx.service;

import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.domain.QueryRequest;
import com.jiebao.platfrom.wx.domain.People;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author qta
 * @since 2020-10-16
 */
public interface IPeopleService extends IService<People> {
    boolean excelImPort(MultipartFile file);

    JiebaoResponse listPage(QueryRequest queryRequest, String DeptId);  //分页查询

    JiebaoResponse listExcel(String deptId,HttpServletResponse response);//导出

    JiebaoResponse saveOrUpdateChile(People people);

    JiebaoResponse lock(Integer status);  //上锁 解锁

    JiebaoResponse checkLock();
}
