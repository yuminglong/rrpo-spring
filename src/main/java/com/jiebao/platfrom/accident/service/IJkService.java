package com.jiebao.platfrom.accident.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiebao.platfrom.accident.daomain.CountTable;
import com.jiebao.platfrom.accident.daomain.Jk;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jiebao.platfrom.accident.daomain.Ql;
import com.jiebao.platfrom.common.domain.QueryRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author qta
 * @since 2020-11-07
 */
public interface IJkService extends IService<Jk> {
    IPage<Jk> listPage(QueryRequest queryRequest, String gac, String dzs);

    boolean importExcel(HttpServletResponse response, String gac, String dzs);

    boolean addOrUpdate(Jk jk, Collection<? extends Serializable> tp);


    boolean delete(String[] idList);

    List<CountTable> countTable(String deptName);  //统计  安装监控数据
}
