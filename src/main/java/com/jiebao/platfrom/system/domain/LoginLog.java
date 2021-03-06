package com.jiebao.platfrom.system.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@TableName("sys_login_log")
@Data
public class LoginLog {
    private static final long serialVersionUID = 1L;
    /**
     * 用户 ID
     */
    private String username;

    private String userId;

    private String deptId;
    /**
     * 登录时间
     */
    private Date loginTime;

    /**
     * 登录地点
     */
    private String location;

    private String ip;
}
