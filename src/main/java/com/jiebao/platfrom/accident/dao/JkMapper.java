package com.jiebao.platfrom.accident.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiebao.platfrom.accident.daomain.CountTable;
import com.jiebao.platfrom.accident.daomain.Jk;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiebao.platfrom.accident.daomain.Ql;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
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

    /**
     * columnName==>数据库名   deptName==>参数名
     *
     * @param columnName
     * @param deptName
     * @return
     */
    //
    @Select("<script>select ${columnName} as name,count(1) as count from accident_jk   " +
            "<where> 1=1 " +
            "<if test=\"deptName != null\"> and gac=#{deptName}</if>" +
            "and ${columnName} is not null " +
            "and ${columnName} != '' " +
            "</where>" +
            "GROUP BY ${columnName}" +
            "</script>")
    List<CountTable> countTable(String columnName, @Param("deptName") String deptName);

    @Select("select gac as name,count(1) as count from accident_jk where gac=#{deptName} and azsjn=#{year}")
    CountTable ByDeptNameGa(@Param("deptName")String deptName,@Param("year") String year);

    @Select("select dzs as name,count(1) as count from accident_jk where dzs=#{deptName} and azsjn=#{year}")
    CountTable ByDeptNameDzs(@Param("deptName") String deptName,@Param("year")String year);

}
