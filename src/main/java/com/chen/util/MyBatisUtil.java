package com.chen.util;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;

public class MyBatisUtil {
    private static SqlSessionFactory factory = null;

    static {
        String config = "mybatis-config.xml";
        try {
            factory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsStream(config));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SqlSession openSession() {
        if (factory == null) {
            throw new RuntimeException("SqlSessionFactory创建失败");
        }
        return factory.openSession();
    }

}
