package com.jiebao.platfrom.wx.service;

import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.wx.entity.DeptLine;
import com.baomidou.mybatisplus.extension.service.IService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author qta
 * @since 2020-10-21
 */
public interface IDeptLineService extends IService<DeptLine> {


    JiebaoResponse getLine(String deptId);

    JiebaoResponse add(DeptLine deptLine);

    JiebaoResponse delete(String[] ids);

}
