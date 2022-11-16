package com.chen.mapper;

import com.chen.domain.DepartmentOneToMany;
import com.chen.util.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class DepartmentMapperTest {

    @Test
    public void selectAll() {
        SqlSession sqlSession = MyBatisUtil.openSession();
        DepartmentMapper mapper = sqlSession.getMapper(DepartmentMapper.class);
        List<DepartmentOneToMany> departmentOneToManies = mapper.selectAll();
        departmentOneToManies.forEach(System.out::println);
    }

    @Test
    public void queryAll() {
        SqlSession sqlSession = MyBatisUtil.openSession();
        DepartmentMapper mapper = sqlSession.getMapper(DepartmentMapper.class);
        List<DepartmentOneToMany> departmentOneToManies = mapper.queryAll();
        departmentOneToManies.forEach(System.out::println);
    }
}