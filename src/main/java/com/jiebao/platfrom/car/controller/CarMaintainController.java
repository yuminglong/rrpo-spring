package com.jiebao.platfrom.car.controller;


import com.jiebao.platfrom.car.domain.Car;
import com.jiebao.platfrom.car.domain.CarMaintain;
import com.jiebao.platfrom.car.service.CarMainService;

import com.jiebao.platfrom.car.service.CarRecordService;
import com.jiebao.platfrom.car.service.CarService;
import com.jiebao.platfrom.common.annotation.Log;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.domain.QueryRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "CarMain")
@Api(tags = "车辆维修记录")
public class CarMaintainController {
   //欠缺  审计部分
    @Autowired
    CarMainService carMainService;
    @Autowired
    CarRecordService recordService;

    @PostMapping(value = "addCarMaintain")
    @ApiOperation(value = "添加车辆送修")
    @Log("添加车辆送修")
    public JiebaoResponse saveOrUpdate(CarMaintain carMaintain) {
        return carMainService.addOrUpdate(carMaintain);
    }

    @DeleteMapping(value = "delete/{id}")
    @Log("删除车辆送修信息")
    public JiebaoResponse delete(Long Id) {
        return new JiebaoResponse().message(carMainService.removeById(Id) ? "删除成功" : "删除失败");
    }

    @GetMapping(value = "list")
    @Log("分页查询车辆送修")
    public JiebaoResponse list(QueryRequest request, Integer state, String CarPlate) { //状态 查询  车牌号直接查询
        return new JiebaoResponse().data(carMainService.getCarMaintainList(request,state,CarPlate)).message("查询成功");
    }
}
