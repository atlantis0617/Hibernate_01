package com.yhy.www.test;

import java.util.Date;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.yhy.www.entity.Student;

public class TestSaveStudent {
    private SessionFactory sessionFactory;
    private Session session;
    private Transaction transaction;
    
    @Test
	public void testSaveStudent() {
        // 生成学生对象
        Student s = new Student(3, "张三丰", "男", new Date(), "武当山");
        session.save(s);
    }
    
    @Before
    public void init() {
        // 创建会话工厂对象
        sessionFactory = new Configuration().configure().buildSessionFactory();
        // 创建会话对象
        session = sessionFactory.openSession();
        // 开始事务
        transaction = session.beginTransaction();
    }
    
    @After
    public void destory () {
        // 提交事务
        transaction.commit();
        // 关闭会话
        session.close();
        // 关闭会话工厂
        sessionFactory.close();
    }
	    
}
