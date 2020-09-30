package com.gis.trans.utils;


import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;

@SuppressWarnings("all")
public class FileOfficeExcel {
    private static Logger logger = LogManager.getLogger(FileOfficeExcel.class);

    /**
     * @param excelPath 文件夹路径
     * @param excelName 文件名字  officeExcelDemo.xlsx
     */
    public void readOfficeExcelFile(String excelPath, String excelName) {
        try {

            File file = new File(excelPath, excelName);

            // 文件转化为输入流
            FileInputStream fi;

            fi = new FileInputStream(file);

            // 操作excel文件需要创建Workbook 对象
            Workbook wb;
            wb = WorkbookFactory.create(fi);

            //获得具体的一个sheet页-这里是第一个
            Sheet sheet = wb.getSheetAt(0);

            // 获取最后一行的行号
            int rowNum = sheet.getLastRowNum() + 1;//这里加1，是因为获取的是序数值，而函数是从0开始计数的
            //logger.info("\n获取第一个 sheet 包含行数为:"+rowNum);

            // 获取某一行的数据-这里是第i行
            for (int i = 2; i < rowNum; i++) {
                Row row = sheet.getRow(i);
                // 获取某一行的列数
                int cellNum = row.getLastCellNum();
                logger.info("\n 第" + i + "行 包含列数为:" + cellNum);
                //输出这一句就可以看到，A1,B1,   A2,B2,C2,D2,E2
                //logger.info("\n行信息："+row.toString());

                //遍历行
                for (int j = 0; j < cellNum; j++) {
                    Cell cell = row.getCell(j);
                    if(cell!=null){
                        cell.setCellType(Cell.CELL_TYPE_STRING);//将表格数据格式转化为字符类型
                        logger.info(cell.getStringCellValue() + " ");
                    }else{
                        logger.error(excelPath+"/"+excelName+" ："+j+" cell error");
                    }

                }

            }


        } catch (FileNotFoundException e1) {
            logger.info(e1.toString());
        } catch (EncryptedDocumentException e) {
            logger.info(e.toString());
        } catch (InvalidFormatException e) {
            logger.info(e.toString());
        } catch (IOException e) {
            logger.info(e.toString());
        }

    }


    /**
     * @param excelPath
     * @param excelName
     * @instruction 在原先的excel文件续写-没有加末尾判断，可能会造成数据被覆
     */
    public void writeOfficeExcelFile(String excelPath, String excelName) {
        Workbook wb;
        File file = new File(excelPath, excelName);
        String fileName = excelPath + "/" + excelName;
        try {
            wb = WorkbookFactory.create(file);
            Sheet sheet = wb.getSheetAt(0);
            Row row = sheet.createRow(1);

            for (int i = 0; i < 10; i++) {
                Cell cell = row.createCell(i);
                cell.setCellValue(i + "");
            }

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            OutputStream out = null;
            try {
                wb.write(os);
                byte[] xls = os.toByteArray();
                out = new FileOutputStream(file);
                out.write(xls);
            } catch (IOException e) {
                logger.info(e.toString());
            } finally {
                try {
                    if (wb != null) {
                        wb.close();
                    }
                    if (out != null) {
                        out.close();
                    }
                    if (os != null) {
                        os.close();
                    }
                    logger.info("覆盖重写文件 " + fileName);
                } catch (IOException e) {
                    logger.info(e.toString());
                }

            }

        } catch (EncryptedDocumentException e) {
            logger.info(e.toString());
        } catch (InvalidFormatException e) {
            logger.info(e.toString());
        } catch (IOException e) {
            logger.info(e.toString());
        }


    }

    /**
     * @param excelPath
     * @param excelName
     * @instruction 程序新建一个excle文件，并且填充一些数据
     * @Datetime 2017年3月27日 下午6:00:08
     */
    public void createOfficeExcelFile(String excelPath, String excelName) {
        //如果文件存在，就先删除
        String fileName = excelPath + "/" + excelName;
        File file = new File(excelPath, excelName);
        if (file.exists()) {    //文件存在
            logger.info("文件已经存在，删除 " + fileName);
            file.delete();
        }
        // 操作excel文件需要实例化 Workbook，借助poi，（office 2007 及之后版本XSSF，向后（下）兼容，xlsx 结尾）
        //HSSF － 提供读写Microsoft Excel格式档案的功能，xls。
        //XSSF － 提供读写Microsoft Excel OOXML格式档案的功能。
        //Workbook wb = new XSSFWorkbook();
        Workbook wb = new XSSFWorkbook();

        // 设置字体格式-略 import org.apache.poi.ss.usermodel.Font;
        // Font font = wb.createFont();

        // 设置单元格样式-略 import org.apache.poi.ss.usermodel.CellStyle;
        // CellStyle style = wb.createCellStyle();

        // 新建工作表
        Sheet sheet = wb.createSheet("测试sheet1");// 创建工作表，名称为test

        // 新建行-第0行，从0开始计数
        Row row = sheet.createRow(0);

        // 给第0行添加数据
        for (int i = 0; i < 10; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(i + "");
            // 结合样式
            // cell.setCellStyle(style);
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        OutputStream out = null;
        try {
            wb.write(os);
            byte[] xls = os.toByteArray();
            out = new FileOutputStream(file);
            out.write(xls);
        } catch (IOException e) {
            logger.info(e.toString());
        } finally {
            try {
                if (wb != null) {
                    wb.close();
                }
                if (out != null) {
                    out.close();
                }
                if (os != null) {
                    os.close();
                }
                logger.info("新建文件 " + fileName);
            } catch (IOException e) {
                logger.info(e.toString());
            }

        }


    }

}