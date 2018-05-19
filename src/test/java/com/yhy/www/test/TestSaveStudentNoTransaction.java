package com.yhy.www.test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.jdbc.Work;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.yhy.www.entity.Student;

public class TestSaveStudentNoTransaction {
	 private SessionFactory sessionFactory;
     private Session session;
     
     /**
      * 不开启事务，数据无法保存到数据库
      */
     @Test
     public void testSaveStudentNoTransaction() {
         // 生成学生对象
         Student s = new Student(5, "张三丰", "男", new Date(), "武当山");
         session.save(s);
     }
     
     /**
      * 不开启事务，使用session.flush()强制同步数据到数据库
      */
     @Test
     public void testSaveStudentByflush() {
         // 生成学生对象
         Student s = new Student(5, "张三丰", "男", new Date(), "武当山");
         session.save(s);
         session.flush();// 当不使用事务时，需使用flush方法，强制提交
     }
     
     /**
      * 开启事务，但不使用commit提交，而是使用session.flush()强制提交
      */
     @Test
     public void testSaveStudentTransaction() {
         // 开启事务
         session.beginTransaction();
         // 生成学生对象
         Student s = new Student(6, "张三丰", "男", new Date(), "武当山");
         session.save(s);
         session.flush();// 不提交事务时，直接使用flush方法，也可强制提交
     }
     
     
     /**
     * 使用SessionDoWork实现JDBC操作
     */
    @Test
     public void 	testSessionDowork() {
    	 final List<String> list = new ArrayList<String>();
    	 session.doWork(new Work() {// 使用dowork方法进行jdbc操作
			@Override
			public void execute(Connection connection) throws SQLException {
				String sql = "select sname from student";
				PreparedStatement ps = (PreparedStatement) connection.prepareStatement(sql);
				ResultSet rs = ps.executeQuery();
				try {
					while (rs.next()) {
	                    String name = rs.getString(1);
	                    list.add(name);
	                }
				} finally {
					if (rs != null) {
	                    rs.close();
	                }
	                if (ps != null) {
	                    ps.close();
	                } 
	                // 此时connection被session管理，不能手动关闭
	                /*if (connection != null) {
	                    connection.close();
	                }*/
				}
			} 
    	 });
    	 for (int i = 0; i < list.size(); i++) {
    	        System.out.println(list.get(i));
	     }
     }
     
     
     
     @Before
     public void init() {
         // 创建会话工厂对象
         sessionFactory = new Configuration().configure().buildSessionFactory();
         // 创建会话对象
         session = sessionFactory.openSession();
     }
     
     
     @After
     public void destory () {
	    // 关闭会话
	    session.close();
	    // 关闭会话工厂
	    sessionFactory.close();
	 }
}
