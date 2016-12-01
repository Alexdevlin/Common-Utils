package com.alexdevlin.conn;
import java.io.IOException;
import java.sql.Connection;  
import java.sql.DriverManager;  
import java.sql.ResultSet;  
import java.sql.SQLException;  
import java.sql.Statement;  
import java.util.Properties;

import org.apache.log4j.jdbc.JDBCAppender;

/**
 * 
 * @Description: 
 * @Author: alexdevlin
 */
public class ConnectionOracle {
    
  Connection con=null;  
  Statement stmt=null;  
  ResultSet rs=null;
  
  public ConnectionOracle()  
  {  
    try {
      Class.forName("oracle.jdbc.driver.OracleDriver");
    } catch (Exception e) {
      // TODO: handle exception
      System.err.println(e.getMessage());
    }
  }
  
  public ResultSet executeQuery(String sql) throws SQLException  
  {
  //加载属性文件，读取数据库连接配置信息
    Properties pro = new Properties();
    try {
        pro.load(JDBCAppender.class.getResourceAsStream("/db.properties"));
    } catch (IOException e) {
        System.out.println("未找到配置文件!!!");
    }
    String url = pro.getProperty("url");
    String user = pro.getProperty("user");
    String password = pro.getProperty("password");
    con=DriverManager.getConnection(url,user,password);  
    Statement stmt=con.createStatement();  
    rs=stmt.executeQuery(sql);  
    return rs;  
  
  }  
 
  public void close() throws SQLException  
  {  
    if(rs!=null){  
      rs.close(); 
    }  
    if(stmt!=null){  
      stmt.close();
    } 
    if(con!=null){  
      con.close();
    }
  }  
  
 
}
