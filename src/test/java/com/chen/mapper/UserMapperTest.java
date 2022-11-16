package com.chen.mapper;

import com.chen.domain.User;
import com.chen.query.UserQuery;
import com.chen.util.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class UserMapperTest {
    private SqlSession sqlSession = MyBatisUtil.openSession();
    private UserMapper mapper = sqlSession.getMapper(UserMapper.class);

    @Test
    public void mappingSelectById() {
        User user = mapper.mappingSelectById(1L);
        sqlSession.close();
        System.out.println(user);
    }

    @Test
    public void mappingSelectAll() {
        List<User> users = mapper.mappingSelectAll();
        sqlSession.close();
        users.forEach(System.out::println);
    }

    @Test
    public void queryAll() {
        List<User> users = mapper.queryAll(new UserQuery("r", 1, 100, 1));
        sqlSession.close();
        users.forEach(System.out::println);
    }

    @Test
    public void batchSave() {
        ArrayList<User> users = new ArrayList<>();
        users.add( new User("zs","123",0,90));
        users.add( new User("ww","456",0,0));
        mapper.batchSave(users);
        sqlSession.commit();
        sqlSession.close();
    }

    @Test
    public void batchDelete() {
        mapper.batchDelete(Arrays.asList(39L,40L,41L));
        sqlSession.commit();
        sqlSession.close();
    }
}