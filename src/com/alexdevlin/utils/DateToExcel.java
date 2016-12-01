package com.alexdevlin.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.jdbc.JDBCAppender;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFHyperlink;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class DateToExcel {
  /** 
   * 将数据导入到excel中 
   * @Description: 
   * @Auther: alexdevlin
   * @param list 
   * @param tableName 
   * @param path 
   * @return 
   * @throws Exception 
   */ 
   public static String TableStructInfoToExcel(List list) throws Exception
   {
     // TODO Auto-generated method stub
     String descFileName="";  
     FileInputStream  fos = null;  
     HSSFRow row = null;         //excel的行 
     HSSFCell cell = null;       // excel的格子单元
     HSSFCellStyle style = null;    //excel样式 
     HSSFFont font = null;          //excel字体 

     String[] tableFiled = {"字段名","类型","允许为空","主键","默认值","备注"};  
       
     try
     {  
       String srcFileName = "src//template.xls";        //模版地址
       
       //加载属性文件，读取数据库表信息
       Properties pro = new Properties();
       try {
           pro.load(JDBCAppender.class.getResourceAsStream("/db.properties"));
       } catch (IOException e) {
           System.out.println("未找到配置文件!!!");
       }
       String user=pro.getProperty("user");     //得到当前用户名
       SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
       descFileName = "newExcel//"+user+df.format(new Date())+".xls";
       
       InputStream is = null;
       OutputStream os = null;
       int len;
       byte []buff = new byte[1024];
       try 
       {
         is = new FileInputStream(srcFileName);
         os = new FileOutputStream(descFileName);

         while((len = is.read(buff)) != -1){
         os.write(buff, 0, len);
         }
       }
       finally{
         is.close();
         os.close();
       }
       
       fos = new FileInputStream (descFileName);
       POIFSFileSystem ps=new POIFSFileSystem(fos); ////使用POI提供的方法得到excel的信息 
         
       HSSFWorkbook wb = new HSSFWorkbook(ps);     //excel的文档对象
       
       FileOutputStream out=new FileOutputStream(descFileName);  //向指定文件写入数据  
       for(int z=0;z<list.size();z++)
       {  
         int currentRowNum = 1;
         HSSFSheet s = wb.createSheet();           //新建excel的工作表单
         style = wb.createCellStyle();  
         font = wb.createFont();
         List listBean = (List) list.get(z);
         //如果有表名解释,就使用表名解释;如果没有解释,则使用表名
         if(listBean.get(2)=="")
         {
           System.out.println("sheetname:"+listBean.get(0).toString());
           wb.setSheetName(z+1,listBean.get(0).toString()+"表");
         }
         else
         {
           System.out.println("sheetname:"+listBean.get(2).toString());
           wb.setSheetName(z+1,listBean.get(2).toString());
         }
         
         //新建一行,再在行上面新建一列  
         row = s.createRow(currentRowNum); 
         int pad = currentRowNum;  
         currentRowNum++;  
           
         //设置样式  
         font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);   //字体加粗
         style.setFont(font); 
         style.setFillForegroundColor((short) 13);// 设置背景色  
         style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);  
         row.setHeight((short)400);              //设置行高
         
         for(int i=0;i<tableFiled.length;i++)
         {  
           cell = row.createCell((short) i);  
           cell.setCellValue(""); 
         }  
       
         row.getCell((short) 0).setCellValue(listBean.get(2).toString());    //显示表名信息
         row.getCell((short) 1).setCellValue(listBean.get(0).toString());    //显示表名
         row.getCell((short) 2).setCellValue("返回索引");      
         
         //设置链接,得到链接行的信息   指定链接地址
         HSSFCell cel = row.getCell((short)2);       
         HSSFHyperlink link = new HSSFHyperlink(HSSFHyperlink.LINK_DOCUMENT);
         link.setAddress("索引!A1");
         cel.setHyperlink(link);
         
         //创建第二行  
         row = s.createRow(currentRowNum);  
         currentRowNum++;  
           
         for(int i=0;i<tableFiled.length;i++)
         {  
           //创建多列并设置每一列的值和宽度  
           cell = row.createCell((short) i);  
           cell.setCellValue(new HSSFRichTextString(tableFiled[i]));  
           s.setColumnWidth((short)i,(short)5000); 
           cell.setCellStyle(style);
         }  
       
          List list2 = (List) listBean.get(1);  
       
         for(int i=0;i<list2.size();i++)
         {  
           row = s.createRow(currentRowNum);  
           currentRowNum++;  
           String[] strings = (String[]) list2.get(i);  
           for(int j=0;j<strings.length;j++){  
           cell = row.createCell((short) j);  
           cell.setCellValue(new HSSFRichTextString(strings[j]));  
           }  
         }  
       
       //合并单元格  
      // s.addMergedRegion(new Region(pad,(short)0,pad,(short)(tableFiled.length-1)));  
       currentRowNum ++;  
     }
     out.flush();
     wb.write(out);
     fos.close();  
     }
     catch (Exception e) 
     {  
       e.printStackTrace();  
       fos.close();  
       throw new Exception(descFileName);  
     }  
       
     return descFileName;  
      
   }

}
