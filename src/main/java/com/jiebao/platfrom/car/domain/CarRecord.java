package com.jiebao.platfrom.car.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("car_record")
public class CarRecord extends Model<CarRecord> implements Serializable {
    @TableId(value = "record_id", type = IdType.AUTO)
    private Long recordId;
    private String recordUserId;   //申请人
    private Date recordCreateTime;  //申请提交时间
    private Date recordDateStart;    //开始用车时间
    private Date recordDateEnd;   //用车结束时间
    private String recordCarId;      //申请所属车辆
    private Date recordBehoof;   //  车辆用途
}
