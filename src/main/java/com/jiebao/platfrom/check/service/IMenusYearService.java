package com.jiebao.platfrom.check.service;

import com.jiebao.platfrom.check.domain.Menus;
import com.jiebao.platfrom.check.domain.MenusYear;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author qta
 * @since 2020-08-05
 */
public interface IMenusYearService extends IService<MenusYear> {
    JiebaoResponse addOrUpdate(MenusYear menusYear);
    JiebaoResponse List(String yearId);//对应的

    JiebaoResponse deleteByListAndYearDate(String[] list);

    JiebaoResponse excel(MultipartFile multipartFile, String yearId);


}
