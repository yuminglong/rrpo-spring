package com.jiebao.platfrom.wx.service.impl;

import com.jiebao.platfrom.wx.domain.DeptLine;
import com.jiebao.platfrom.wx.dao.DeptLineMapper;
import com.jiebao.platfrom.wx.service.IDeptLineService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author qta
 * @since 2020-09-10
 */
@Service
public class DeptLineServiceImpl extends ServiceImpl<DeptLineMapper, DeptLine> implements IDeptLineService {


    @Override
    public void setDeptLine(String deptId, String qunId) {
        if (!(this.baseMapper.exist(qunId, deptId) == null)) {
            return;  //已存在不入
        }
        DeptLine deptLine = new DeptLine();
        deptLine.setDeptId(deptId);
        deptLine.setQunId(qunId);
        Integer integer = this.baseMapper.maxNumber(qunId);
        deptLine.setNumber((integer == null ? 0 : integer) + 1);
        save(deptLine);
    }

    @Override
    public String getDownDeptId(String qunId, String deptId) {
        DeptLine deptLine = this.baseMapper.selectDeptLine(qunId, deptId); //本级
        DeptLine deptLine1 = this.baseMapper.selectDeptLine(qunId, deptLine.getNumber() - 1);  //下级部门
        return deptLine1.getDeptId();
    }
}
