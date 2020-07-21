package com.jiebao.platfrom.room.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiebao.platfrom.room.dao.UsersMapper;
import com.jiebao.platfrom.room.domain.Users;
import com.jiebao.platfrom.room.service.IUsersService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author qta
 * @since 2020-07-20
 */
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements IUsersService {

}
