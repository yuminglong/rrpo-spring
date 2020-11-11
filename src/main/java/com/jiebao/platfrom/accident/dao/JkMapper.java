package com.jiebao.platfrom.accident.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiebao.platfrom.accident.daomain.Jk;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiebao.platfrom.accident.daomain.Ql;
import org.apache.ibatis.annotations.*;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author qta
 * @since 2020-11-07
 */
public interface JkMapper extends BaseMapper<Jk> {
    @Select(" select ID as idF,ID,PK as pk,gac,dzs,xsq,xzjd,xlmc,tllc,azfw,tljl,dlmc,dllc,azbw,sl,jkqlx,jkfw,jksjzl,jkysgn,jkgd,blts,lwdw,jsdw,azsjn,azsj,gldw,sydw,pcs,sdpcs,bz,delflag,username,datalock,creat_time from accident_jk ${ew.customSqlSegment}")
    @Results({
            @Result(property = "tpFile", column = "idF", one = @One(select = "com.jiebao.platfrom.system.dao.FileMapper.fileList"))
    })
    IPage<Jk> pageList(Page<Jk> page, @Param("ew") LambdaQueryWrapper<Jk> ew);
}
