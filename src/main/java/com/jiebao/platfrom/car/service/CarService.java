package com.jiebao.platfrom.car.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jiebao.platfrom.car.domain.Car;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.domain.QueryRequest;
import com.jiebao.platfrom.demo.domain.Demo;

/**
 * 演示代码 - service
 *
 * @Author Sinliz
 * @Date 2020/3/12 10:00
 */

public interface CarService extends IService<Car> {

    IPage<Car> getCarList(QueryRequest request, Integer state);  //分页查询

    JiebaoResponse addOrUpdate(Car car);
}
