package com.geovis.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressWarnings("all")
public class UDFileUtil {
    private static Logger logger = LoggerFactory.getLogger(UDFileUtil.class);
    /***
     * 保存文件
     * @param file
     * @return
     */
    public static boolean saveFile(MultipartFile file,String path) {
        // 判断文件是否为空
        if (!file.isEmpty()) {
            String filePath="";
            try {

                filePath =path+file.getOriginalFilename();
                FileUtil.mkDir(filePath);
                // 转存文件
                file.transferTo(new File(filePath));
                //判断该文件是否为图片文件，是图片文件则生成对应的缩略图
                if(ImageUtil.isImage(filePath)){
                    ImageUtil.thumbnailImage(filePath, 100, 150,ImageUtil.DEFAULT_PREVFIX,ImageUtil.DEFAULT_FORCE);
                }
                return true;
            } catch (Exception e) {
                logger.error("上传文件失败!文件路径：path="+filePath);
                e.printStackTrace();
            }
        }else{
            logger.warn("上传文件为空：文件名为："+file.getOriginalFilename());
            return false;
        }
        return false;
    }

    /***
     * 根据时间返回当前年月日组成的路径
     * @param date
     * @return
     */
    public static String getDatePath(Date date) {
        if(date==null){
            date = new Date();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String dateNowStr = sdf.format(date);
        return dateNowStr;
    }

    /**
     * 帶文件名全路徑下載
     * @param filename
     * @return
     */
    public static ResponseEntity  downloadFile(String filename,String name) {

        InputStream inputStream = null;
        ByteArrayOutputStream bos = null;
        BufferedInputStream bis = null;
        //String filename = "E:\\aa\\a.txt";

        try {
            inputStream = new FileInputStream(filename);
            bis = new BufferedInputStream(inputStream);
            bos = new ByteArrayOutputStream();
            int date = -1;
            while ((date = bis.read()) != -1) {
                bos.write(date);
            }
            HttpHeaders headers = new HttpHeaders();
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");
            headers.add("charset", "utf-8");
            //设置下载文件名
            filename = URLEncoder.encode(name, "UTF-8");
            headers.add("Content-Disposition", "attachment;filename=\"" + name + "\"");

            Resource resource = new InputStreamResource(new ByteArrayInputStream(bos.toByteArray()));
            return ResponseUtil.success(headers,resource);
        } catch (IOException e) {
            logger.error("下载文件失败！"+filename);
            return ResponseUtil.failure("下载文件失败！");
        } finally {
            try {
                if(inputStream!=null){
                    inputStream.close();
                }
                if(bos!=null){
                    bos.close();
                }
                if(bis!=null){
                    bis.close();
                }

            } catch (IOException e) {
                logger.error("下载文件关闭流失败！"+filename);
            }
        }
    }
}
