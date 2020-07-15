package com.jiebao.platfrom.car.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jiebao.platfrom.car.domain.Car;
import com.jiebao.platfrom.car.domain.CarRecord;
import com.jiebao.platfrom.common.domain.QueryRequest;

public interface CarRecordService extends IService<CarRecord> {

    IPage<Car> getCarList(QueryRequest request, Integer state);
}
