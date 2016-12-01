package com.alexdevlin.sql;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.jdbc.JDBCAppender;

import com.alexdevlin.conn.ConnectionOracle;

public class Info {
  /**
   * 获取所有表
   * @Description: 
   * @Auther: alexdevlin
   */
  public static List getTableList()
  {
    String sql="";
    
    //加载属性文件，读取数据库表信息
    Properties pro = new Properties();
    try {
        pro.load(JDBCAppender.class.getResourceAsStream("/db.properties"));
    } catch (IOException e) {
        System.out.println("未找到配置文件!!!");
    }
    String tablename=pro.getProperty("tablename");
    
    if(tablename.toLowerCase().trim().equals("all"))
    {
      sql = "select table_name,comments from user_tab_comments  where Table_type='TABLE' order by Table_Name";  
      System.out.println("正在读取数据库中所有表..");     
    }
    else
    {
      String table[] = tablename.split(",");
      sql = "select table_name,comments from user_tab_comments  where Table_type='TABLE' and (" ;
      for(int i=0;i<table.length;i++){
            sql += " table_name='"+table[i].toUpperCase().trim()+"'";
            if(i<table.length-1){
                sql += " or";
            }
      }
      sql += ") order by Table_Name";
      System.out.println("正在读取您所需部分表..");
    }
     return getResult(sql,2);  
  }

  /**
   * 获取表结构信息
   * @Description: 
   * @Auther: alexdevlin
   */
    public static List getStructOfTable(String tableName)
    {  
      String sql = "SELECT u.column_name as 字段名 ,u.data_type||'('||u.data_length||')' as 类型,case u.nullable when 'N' then '否' when 'Y' then '是' end 允许为空,  case  when u.column_name in(select col.column_name from user_constraints con,user_cons_columns col where con.constraint_name=col.constraint_name and con.constraint_type='P' and col.table_name='"+tableName+"') then '是' else '' end 主键,u.data_default as 默认值,c.comments as 备注 FROM user_tab_columns u,user_col_comments c"+  
                   " WHERE u.table_name='"+tableName+"' and u.table_name=c.table_name and c.column_name=u.column_name";  
      return getResult(sql,6);  
    }  
    
    
    /**
     * 获取结果的公共类
     * @Description: 
     * @Auther: alexdevlin
     */
    private static List getResult(String sql, int length) {
      // TODO Auto-generated method stub
      List list=new ArrayList();
      ResultSet rs =null;
      ConnectionOracle c=new ConnectionOracle();
      try 
      {
        rs=c.executeQuery(sql);
        while(rs.next())
        {
            String[] string =new String[length];
            for(int i=1;i<length+1;i++)
            {
              string[i-1] = rs.getString(i);
            }
            list.add(string); 
        }
        c.close(); 
      }
      catch (Exception e)
      {
        // TODO: handle exception
        e.printStackTrace();
      }
      return list;
    }
    
}
