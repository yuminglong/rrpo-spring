package com.jiebao.platfrom.attendance.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiebao.platfrom.attendance.dao.RecordsMapper;
import com.jiebao.platfrom.attendance.daomain.Record;
import com.jiebao.platfrom.attendance.service.IRecordsService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author qta
 * @since 2020-08-07
 */
@Service
public class RecordsServiceImpl extends ServiceImpl<RecordsMapper, Record> implements IRecordsService {

}
