package com.jiebao.platfrom.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.system.domain.LoginLog;

import java.util.Date;

public interface LoginLogService extends IService<LoginLog> {

    void saveLoginLog(LoginLog loginLog);

    JiebaoResponse lists(String deptParentId, Date startDate, Date endDate);

    JiebaoResponse listUsers(String deptId, Date startDate, Date endDate);
}
