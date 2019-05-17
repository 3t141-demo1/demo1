package com.xinshi.smbms.util;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.support.PersistenceExceptionTranslator;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;

public class MybaitsUtil extends SqlSessionTemplate {

    @Autowired
    @Qualifier("sqlSessionFactory")
    private static SqlSessionFactory sqlSessionFactory;
    @Autowired
    private static SqlSession sqlSession;

    public MybaitsUtil(SqlSessionFactory sqlSessionFactory) {
        super(sqlSessionFactory);
    }

    public MybaitsUtil(SqlSessionFactory sqlSessionFactory, ExecutorType executorType) {
        super(sqlSessionFactory, executorType);
    }

    public MybaitsUtil(SqlSessionFactory sqlSessionFactory, ExecutorType executorType, PersistenceExceptionTranslator exceptionTranslator) {
        super(sqlSessionFactory, executorType, exceptionTranslator);
    }

    public static SqlSession openSession(){
        if(sqlSession==null){
            sqlSession = sqlSessionFactory.openSession(true);
        }
        return sqlSession;
    }

    public static  void closeSession(){
        if(null!=sqlSession){
            sqlSession.close();
        }
    }


}
