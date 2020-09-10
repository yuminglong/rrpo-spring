package com.jiebao.platfrom.check.service;

import com.jiebao.platfrom.check.domain.YearBindMenus;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jiebao.platfrom.common.domain.JiebaoResponse;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author qta
 * @since 2020-09-03
 */
public interface IYearBindMenusService extends IService<YearBindMenus> {
    JiebaoResponse add(String yearID,String[] menusId);

    JiebaoResponse delete(String[] ids);

    JiebaoResponse list(String yearId);
}
