package com.jiebao.platfrom.car.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiebao.platfrom.car.dao.CarMapper;
import com.jiebao.platfrom.car.domain.Car;
import com.jiebao.platfrom.car.service.CarService;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.domain.QueryRequest;
import com.jiebao.platfrom.demo.domain.Demo;
import com.jiebao.platfrom.railway.dao.InformMapper;
import com.jiebao.platfrom.railway.domain.Inform;
import com.jiebao.platfrom.railway.service.InformService;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service("CarService")
public class CarServiceImpl extends ServiceImpl<CarMapper, Car> implements CarService {


    @Override
    public IPage<Car> getCarList(QueryRequest request, Integer state) {
        QueryWrapper<Car> carQueryWrapper = new QueryWrapper<>();
        carQueryWrapper.orderByDesc("car_create_time");
        if (state != null) {
            carQueryWrapper.eq("car_state", state);
        }
        Page<Car> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.baseMapper.selectPage(page, carQueryWrapper);
    }

    @Override
    public JiebaoResponse addOrUpdate(Car car) {
        JiebaoResponse jiebaoResponse = new JiebaoResponse();
        QueryWrapper<Car> carQueryWrapper = new QueryWrapper<>();
        carQueryWrapper.eq("car_plate", car.getCarPlate());
        if (this.baseMapper.selectList(carQueryWrapper).size() != 0) {   //判断是否此车牌  已存在
            return jiebaoResponse.message("车牌重叠");
        }
        return jiebaoResponse.message(saveOrUpdate(car) ? "操作成功" : "操作失败");
    }
}
