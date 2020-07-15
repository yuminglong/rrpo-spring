package com.jiebao.platfrom.car.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiebao.platfrom.car.dao.CarMaintainMapper;
import com.jiebao.platfrom.car.domain.Car;
import com.jiebao.platfrom.car.domain.CarMaintain;
import com.jiebao.platfrom.car.service.CarMainService;
import com.jiebao.platfrom.car.service.CarService;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.domain.QueryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CarMaintainServiceImpl extends ServiceImpl<CarMaintainMapper, CarMaintain> implements CarMainService {


    @Autowired
    CarService carService;

    @Override
    public IPage<CarMaintain> getCarMaintainList(QueryRequest request, Integer state, String CarPlate) {  //参数状态   车牌号
        QueryWrapper<CarMaintain> carQueryWrapper = new QueryWrapper<>();
        carQueryWrapper.orderByDesc("maintain_create_time");
        if (state != null) {
            carQueryWrapper.eq("maintain_state", state);
        }
        if (CarPlate != null) {
            QueryWrapper<Car> carQueryWrapper1 = new QueryWrapper<>();
            carQueryWrapper1.eq("car_plate", CarPlate);
            Car car = carService.getOne(carQueryWrapper1);  //通过车牌号获得对应的车辆
            System.out.println(car);
            carQueryWrapper.eq("maintain_car_id", car.getCarId());
        }
        Page<CarMaintain> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.baseMapper.selectPage(page, carQueryWrapper);
    }

    @Override
    public JiebaoResponse addOrUpdate(CarMaintain carMaintain) {   //添加送修   修改车辆状态
        JiebaoResponse jiebaoResponse = new JiebaoResponse();
        Car car = carService.getById(carMaintain.getMaintainCarId());
        if (car.getCarState() == 1) {  //若已经送修
            return jiebaoResponse.message("车辆已送修");
        }
        if (!saveOrUpdate(carMaintain)) {
            return jiebaoResponse.message("操作失败");
        }
        if (carMaintain.getMaintainState() == 0) {   //提交  的时候修改状态
            car.setCarState(1);
        } else if (carMaintain.getMaintainState() == 2 || carMaintain.getMaintainState() == 4) {  //审核不通过 或者  车辆维修完成时 修改为 车辆空闲状态
            car.setCarState(2);
        }
        return jiebaoResponse.message(carService.saveOrUpdate(car) ? "操作成功" : "操作失败");
    }
}
