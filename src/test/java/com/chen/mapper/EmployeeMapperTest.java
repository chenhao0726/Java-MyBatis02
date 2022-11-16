package com.chen.mapper;

import com.chen.domain.DepartmentManyToOne;
import com.chen.domain.EmployeeManyToOne;
import com.chen.util.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class EmployeeMapperTest {

    /**
     * 多对一
     * 嵌套结果
     */
    @Test
    public void selectAllManyToOne() {
        SqlSession sqlSession = MyBatisUtil.openSession();
        EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
        List<EmployeeManyToOne> employeeManyToOnes = mapper.selectAllManyToOne();
        employeeManyToOnes.forEach(employeeManyToOne -> {
            System.out.println(employeeManyToOne);
            System.out.println(employeeManyToOne.getDept());
        });
        sqlSession.close();
    }

    /**
     * 多对一
     * 嵌套查询
     */
    @Test
    public void queryAllManyToOne() {
        SqlSession sqlSession = MyBatisUtil.openSession();
        EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
        List<EmployeeManyToOne> employeeManyToOnes = mapper.queryAllManyToOne();
        employeeManyToOnes.forEach(employeeManyToOne -> {
            System.out.println(employeeManyToOne);
            System.out.println(employeeManyToOne.getDept());
        });
    }

    /**
     * 多对一
     * 保存插入
     */
    @Test
    public void insertTest() {
        SqlSession sqlSession = MyBatisUtil.openSession();
        EmployeeMapper employeeMapper = sqlSession.getMapper(EmployeeMapper.class);
        DepartmentMapper departmentMapper = sqlSession.getMapper(DepartmentMapper.class);
        // 先保存一的一方
        DepartmentManyToOne dept = new DepartmentManyToOne("研发部");
        departmentMapper.insertDept(dept);
        // 再保存多的一方
//        ArrayList<EmployeeManyToOne> employeeManyToOnes = new ArrayList<>();
        EmployeeManyToOne emp1 = new EmployeeManyToOne("小明", 23, dept);
//        EmployeeManyToOne emp2 = new EmployeeManyToOne("小明2", 22, dept);
//        EmployeeManyToOne emp3 = new EmployeeManyToOne("小明3", 21, dept);
//        employeeManyToOnes.add(emp1);
//        employeeManyToOnes.add(emp2);
//        employeeManyToOnes.add(emp3);
        employeeMapper.insertEmp(emp1);
        sqlSession.commit();
        sqlSession.close();
    }

}