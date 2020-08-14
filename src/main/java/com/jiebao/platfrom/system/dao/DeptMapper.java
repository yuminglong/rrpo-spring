package com.jiebao.platfrom.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiebao.platfrom.railway.domain.Address;
import com.jiebao.platfrom.system.domain.Dept;
import org.apache.ibatis.annotations.Select;

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

}