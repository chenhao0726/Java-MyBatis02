package com.chen.dao;

import com.chen.domain.User;

import java.util.List;

public interface IUserDao {

    /**
     * 根据id查询
     * @param id
     * @return
     */
    User selectById(Long id);

    /**
     * 查询所有
     * @return
     */
    List<User> selectAll();
}
