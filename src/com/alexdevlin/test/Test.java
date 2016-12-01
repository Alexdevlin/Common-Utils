package com.alexdevlin.test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.text.DefaultEditorKit.BeepAction;

import org.apache.log4j.Logger;

import com.alexdevlin.sql.*;
import com.alexdevlin.utils.*;
/**
 * 将oracle中的数据表结构导入到excel中保存
 * @Description 
 * @Author: alexdevlin
 */
public class Test {
  static Logger log = Logger.getLogger(BeepAction.class.getName());
  public static void main(String[] args) {
    String result="";
    List listAll=new ArrayList();
    try 
    {
      List tableList=new Info().getTableList();
      log.info("数据库表读取完成");
      for(int i=0;i<tableList.size();i++)
      {  
        String[] strings = (String[]) tableList.get(i);  
        String tableName = strings[0].toString();
        String comments="";
        if(strings[1]==null||strings[1].trim().length()==0)
        {
          comments="";
        }
        else
        {
          comments=strings[1].toString();
        }
        List list = new ArrayList();  
        list.add(tableName);
        list.add(new Info().getStructOfTable(tableName));
        list.add(comments);
        log.info("正在生成表"+tableName+"的结构");  
        System.out.println("正在生成表"+tableName+"的结构");
        System.out.println(comments);
        listAll.add(list); 
      }
      result =DateToExcel.TableStructInfoToExcel(listAll);  
      log.info("数据库中表结构导入已完成"); 
      System.out.println("数据库中表结构导入已完成");
    } 
    catch (Exception e) 
    {
      e.printStackTrace(); 
      File file = new File(e.getMessage().toString());  
      e.printStackTrace();
      if(!file.exists()){  
        
        log.info("您所指定路径的文件不存在!");  
          
        }  
    }
    log.info("新文件:"+result);
    System.out.println("新文件:"+result);
  }
}
