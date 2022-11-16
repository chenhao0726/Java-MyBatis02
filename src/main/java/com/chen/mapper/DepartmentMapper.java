package com.chen.mapper;

import com.chen.domain.DepartmentManyToOne;
import com.chen.domain.DepartmentOneToMany;

import java.util.List;

public interface DepartmentMapper {

    /**
     * 多对一嵌套查询
     * @param id
     * @return
     */
    DepartmentManyToOne selectByDid(Long id);

    /**
     * 多对一 保存
     * @param departmentManyToOne
     */
    void insertDept(DepartmentManyToOne departmentManyToOne);

    /**
     * 一对多 嵌套结果查询
     * @return
     */
    List<DepartmentOneToMany> selectAll();

    /**
     * 一对多 嵌套查询
     * @return
     */
    List<DepartmentOneToMany> queryAll();
}
