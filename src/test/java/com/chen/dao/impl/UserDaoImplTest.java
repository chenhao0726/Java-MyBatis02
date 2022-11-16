package com.chen.dao.impl;

import com.chen.dao.IUserDao;
import com.chen.domain.User;
import org.junit.Test;

import java.util.List;


public class UserDaoImplTest {
    private IUserDao dao = new UserDaoImpl();

    @Test
    public void selectById() {
        User user = dao.selectById(1L);
        System.out.println(user);
    }

    @Test
    public void selectAll() {
        List<User> users = dao.selectAll();
        users.forEach(System.out::println);
    }
}