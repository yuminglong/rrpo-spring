package com.jiebao.platfrom.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiebao.platfrom.railway.domain.Address;
import com.jiebao.platfrom.system.domain.Dept;
import org.apache.ibatis.annotations.Select;

import java.io.Serializable;
import java.util.List;

public interface DeptMapper extends BaseMapper<Dept> {

	/**
	 * 递归删除部门
	 *
	 * @param deptId deptId
	 */
	void deleteDepts(String deptId);

	@Select("SELECT * FROM `sys_dept` d where d.dept_id = #{deptId}")
	Dept getById(String deptId);
	@Select("SELECT dept_id FROM `sys_dept` d where d.dept_id = #{deptId}")
	String getIdById(String deptId);


	@Select("SELECT dept_name FROM `sys_dept` d where d.dept_id = #{deptId}")
	String getByName(String deptId);

	@Select("SELECT * FROM `sys_dept` d where d.dept_name = #{deptName}")
	Dept getByNewName(String deptName);

//	@Select("SELECT * FROM `sys_dept` d where d.dept_name = #{deptName}  ")
//	Dept getByNewNames(String deptName);

//	@Select("select dept_id from sys_dept where dept_name=#{name}")
//	String queryIdByName(String name);
//
//	@Select("select dept_id from sys_dept where dept_name=#{name} and parent_id=#{id}")
//	String queryIdByName1(String name,String id);

	@Select("SELECT * FROM `sys_dept`  where rank = 0 order by order_num ")
	List<Dept> selectRankZero();
}