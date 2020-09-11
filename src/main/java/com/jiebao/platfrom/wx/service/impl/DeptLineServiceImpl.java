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
        deptLine.setNumber(this.baseMapper.maxNumber(qunId) + 1);
        save(deptLine);
    }
}
