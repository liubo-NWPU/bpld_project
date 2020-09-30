package com.geovis.web.controller.system;

import com.alibaba.fastjson.JSON;
import com.geovis.core.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@SuppressWarnings("all")
@RestController
@RequestMapping("/system")
@CrossOrigin
public class UploadController {

    private Logger logger = LoggerFactory.getLogger(UploadController.class);
    @Value("${system.file.uploadfolder}")
    private String UPLOAD_FOLDER;

    //   @PostMapping("/uploadfile")
//    public ResponseEntity fileUpload(HttpServletRequest request,YhYhfj yhYhfj) throws FileUploadException {
//
//        //多媒体上传
//        List<MultipartFile> files = ((MultipartHttpServletRequest) request)
//                .getFiles("file");
//        //表单上传
//        Map<String,String[]> map= request.getParameterMap();
//
//
//        String fileNameResult ="";
//        return null;
//
//    }

    @PostMapping("/upload")
    public ResponseEntity singleFileUpload(MultipartFile file) {
        logger.debug("传入的文件参数：{}", JSON.toJSONString(file, true));
        if (Objects.isNull(file) || file.isEmpty()) {
            logger.error("文件为空");
            return ResponseUtil.failure("文件为空，请重新上传!");
        }
        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOAD_FOLDER + file.getOriginalFilename());
            //如果没有files文件夹，则创建
            if (!Files.isWritable(path)) {
                Files.createDirectories(Paths.get(UPLOAD_FOLDER));
            }
            //文件写入指定路径
            Files.write(path, bytes);
            logger.debug("文件写入成功...");
            return ResponseUtil.success("文件上传成功!");
        } catch (IOException e) {
            logger.error("文件上传失败！"+ e.toString());
            return ResponseUtil.failure("上传文件出现异常！"+e.getMessage());
        }
    }


    @GetMapping("/download")
    public ResponseEntity downloadFile() {

        InputStream inputStream = null;
        ByteArrayOutputStream bos = null;
        BufferedInputStream bis = null;
        String filename = "E:\\aa\\test.txt";
        String name ="tes2423t.txt";

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
            //filename = URLEncoder.encode(filename, "UTF-8");
            headers.add("Content-Disposition", "attachment;filename=\"" + name + "\"");

            Resource resource = new InputStreamResource(new ByteArrayInputStream(bos.toByteArray()));
            return ResponseUtil.success(headers,resource);
        } catch (IOException e) {
            logger.error("下载文件失败！"+filename);
            return ResponseUtil.failure("下载文件失败！"+e.getMessage());
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
