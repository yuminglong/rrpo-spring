package com.jiebao.platfrom.car.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jiebao.platfrom.car.domain.CarMaintain;
import com.jiebao.platfrom.common.domain.JiebaoResponse;
import com.jiebao.platfrom.common.domain.QueryRequest;

public interface CarMainService extends IService<CarMaintain> {
    JiebaoResponse addOrUpdate(CarMaintain carMaintain); //修改添加

    IPage<CarMaintain> getCarMaintainList(QueryRequest request, Integer state, String CarPlate);//分页查询
}
