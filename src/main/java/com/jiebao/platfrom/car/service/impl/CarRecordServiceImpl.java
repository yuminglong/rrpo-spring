package com.jiebao.platfrom.car.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiebao.platfrom.car.dao.CarMapper;
import com.jiebao.platfrom.car.dao.CarRecordMapper;
import com.jiebao.platfrom.car.domain.Car;
import com.jiebao.platfrom.car.domain.CarRecord;
import com.jiebao.platfrom.car.service.CarRecordService;
import com.jiebao.platfrom.car.service.CarService;
import com.jiebao.platfrom.common.domain.QueryRequest;
import org.springframework.stereotype.Service;

@Service
public class CarRecordServiceImpl extends ServiceImpl<CarRecordMapper, CarRecord> implements CarRecordService {

    @Override
    public IPage<Car> getCarList(QueryRequest request, Integer state) {
        return null;
    }
}
