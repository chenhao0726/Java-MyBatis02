package com.chen.dao.impl;

import com.chen.dao.IUserDao;
import com.chen.domain.User;
import com.chen.util.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class UserDaoImpl implements IUserDao {
    @Override
    public User selectById(Long id) {
        SqlSession sqlSession = MyBatisUtil.openSession();
        User user = sqlSession.selectOne("com.chen.mapper.UserMapper.selectById", id);
        sqlSession.close();
        return user;
    }

    @Override
    public List<User> selectAll() {
        SqlSession sqlSession = MyBatisUtil.openSession();
        List<User> users = sqlSession.selectList("com.chen.mapper.UserMapper.selectAll");
        sqlSession.close();
        return users;
    }
}
