package com.chen.mapper;

import com.chen.domain.User;
import com.chen.query.UserQuery;

import java.util.List;

public interface UserMapper {

    /**
     * 根据id查询
     * @param id
     * @return
     */
    User mappingSelectById(Long id);

    /**
     * 查询所有
     * @return
     */
    List<User> mappingSelectAll();

    /**
     * 高级查询和动态sql
     * @return
     */
    List<User> queryAll(UserQuery userQuery);

    /**
     * 批量插入
     * @param users
     * @return
     */
    int batchSave(List<User> users);

    /**
     * 批量删除
     * @param ids
     * @return
     */
    int batchDelete(List<Long> ids);
}
