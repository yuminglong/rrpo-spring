package com.jiebao.platfrom.car.controller;

import com.jiebao.platfrom.car.domain.Car;
import com.jiebao.platfrom.car.service.CarService;
import com.jiebao.platfrom.common.annotation.Log;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.domain.QueryRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "car")
@Api(tags = "车辆信息")
public class CarController {

    @Autowired
    private CarService carService;

    @PostMapping(value = "addCar")
    @ApiOperation(value = "添加车辆信息")
    @Log("增加车辆")
    public JiebaoResponse saveOrUpdate(Car car) {
        return carService.addOrUpdate(car);
    }

    @DeleteMapping(value = "delete/{id}")
    @Log("删除车辆信息")
    public JiebaoResponse delete(Long carId) {
        return new JiebaoResponse().message(carService.removeById(carId) ? "删除成功" : "删除失败");
    }

    @GetMapping(value = "list")
    @Log("分页查询车辆")
    public JiebaoResponse list(QueryRequest request, Integer state) { //0正在使用  1车辆送修  2车辆空闲
        return new JiebaoResponse().data(carService.getCarList(request, state)).message("查询成功");
    }
}
