package com.chen.mapper;

import com.chen.domain.EmployeeManyToOne;
import com.chen.domain.EmployeeOneTwoMany;

import java.util.List;

public interface EmployeeMapper {

    /**
     * 多对一嵌套结果查询
     * @return
     */
    List<EmployeeManyToOne> selectAllManyToOne();

    /**
     * 多对一嵌套查询
     * @return
     */
    List<EmployeeManyToOne> queryAllManyToOne();

    /**
     * 多对一 保存插入
     * @param employeeManyToOnes
     */
    void insertEmp(EmployeeManyToOne employeeManyToOnes);

    /**
     * 多对一 修改
     * @param employeeManyToOne
     */
    void updateById(EmployeeManyToOne employeeManyToOne);

    /**
     * 多对一 删除
     * @param id
     */
    void deleteById(Long id);

    /**
     * 一对多 嵌套查询
     * @param id
     * @return
     */
    EmployeeOneTwoMany selectById(Long id);
}
