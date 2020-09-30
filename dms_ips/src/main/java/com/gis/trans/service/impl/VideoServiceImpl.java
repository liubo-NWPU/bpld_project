package com.gis.trans.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.gis.trans.constant.CRUD;
import com.gis.trans.dao.VideoDao;
import com.gis.trans.model.MineArea;
import com.gis.trans.model.ResponseModel;
import com.gis.trans.model.Video;
import com.gis.trans.service.MineAreaService;
import com.gis.trans.service.VideoService;
import com.gis.trans.utils.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VideoServiceImpl extends BaseServiceImpl<Video, Long> implements VideoService {
    @Autowired
    private VideoDao videoDao;

    @Autowired
    private MineAreaService mineAreaService;

    @Value("${system.file.manager}")
    private String managerUrl;

    @Override
    public JpaRepository<Video, Long> getDao() {
        return videoDao;
    }

    @Override
    public ResponseModel addVideo(Video Video) {
        ResponseModel responseModel = new ResponseModel();
        try {
            Map map = new HashMap();
            map.put("id", Video.getRadarId().toString());
            String s = HttpUtil.sendGet(managerUrl+"/manager/rest/filecatalog/getById", map);
            if (s!=null){
                Short fileType = JSONObject.parseObject(s).getShort("fileType");
                if (fileType!=0){
                    responseModel.setSuccess(false);
                    responseModel.setMessage("非文件夹禁止添加雷达信息");
                    return responseModel;
                }
            }
            Map map1 = new HashMap();
            map1.put("id", Video.getRadarId().toString());
            String s1 = HttpUtil.sendGet(managerUrl+"/manager/rest/filecatalog/getParentById", map1);
            if (s1==null){ //表示在一级目录下
                responseModel.setSuccess(false);
                responseModel.setMessage("非矿场文件夹下禁止添加雷达信息");
                return responseModel;
            }else {
                Long parentId = JSONObject.parseObject(s1).getLong("id");
                MineArea mineArea = mineAreaService.searchByMineId(parentId,null);
                if (mineArea==null){
                    responseModel.setSuccess(false);
                    responseModel.setMessage("非矿场文件夹下禁止添加雷达信息");
                    return responseModel;
                }
            }
            MineArea mineArea = mineAreaService.searchByMineId(Video.getRadarId(),null);
            if (mineArea!=null){
                responseModel.setSuccess(false);
                responseModel.setMessage("该文件夹已添加矿场信息");
                return responseModel;
            }
            videoDao.save(Video);
            responseModel.setSuccess(true);
            responseModel.setMessage(CRUD.INSERT_SUCCESS);
        } catch (Exception e) {
            responseModel.setSuccess(false);
            responseModel.setMessage(CRUD.INSERT_FAIL);
            responseModel.setException(e.getMessage());
        }
        return responseModel;
    }


    @Override
    public ResponseModel deleteAllVideo(Long[] ids) {
        ResponseModel responseModel = new ResponseModel();
        try {
            for (Long id : ids) {
                deleteVideo(id);
            }
            responseModel.setSuccess(true);
            responseModel.setMessage(CRUD.DELETE_SUCCESS);
        } catch (Exception e) {
            responseModel.setSuccess(false);
            responseModel.setMessage(CRUD.DELETE_FAIL);
            responseModel.setException(e.getMessage());
        }
        return responseModel;
    }

    @Override
    public ResponseModel deleteVideo(Long id) {
        ResponseModel responseModel = new ResponseModel();
        try {
            Video Video = videoDao.findById(id);
            if (Video != null) {
                delete(Video);
            }
            responseModel.setSuccess(true);
            responseModel.setMessage(CRUD.DELETE_SUCCESS);
        } catch (Exception e) {
            responseModel.setSuccess(false);
            responseModel.setMessage(CRUD.DELETE_FAIL);
            responseModel.setException(e.getMessage());
        }
        return responseModel;
    }

    @Override
    public List<Video> searchByName(String name) {
        return videoDao.findByName(name);
    }

    @Override
    public Video searchByVideoId(Long VideoId) {
        return videoDao.findById(VideoId);
    }

    @Override
    public List<Video> findByVideoKey(String VideoKey) {
       return videoDao.findByName(VideoKey);
    }

    public void uploadVideo(InputStream inputStream){
        File saveDirectory = new File("E:\\data\\video");
        // 创建文件夹
        if (!saveDirectory.exists() && !saveDirectory.isDirectory()) {
            saveDirectory.mkdirs();
        }
        // 转byte
        byte[] b = readAsBytes(inputStream);
        OutputStream out = null;
        String filePath = saveDirectory.getAbsolutePath()+"\\s.avi";
        File file = new File(filePath);
        try{
            out = new FileOutputStream(file);
            out.write(b);
            out.flush();

            // 第一帧图片路径
         //   grabberVideoFramer(saveDirectory.getAbsolutePath(),file.getName() );
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if(null != out){
                    out.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 二进制读取
    public static byte[] readAsBytes(InputStream in) {
        byte[] buffer = new byte[0];
        try {
            buffer = new byte[in.available()];
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            in.read(buffer);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return buffer;
    }

   /* public String grabberVideoFramer(String videoFileName, String videoPath){

        //最后获取到的视频的图片的路径
        String videPicture="";
        String pivStr = "";
        //Frame对象
        Frame frame = null;
        //标识
        int flag = 0;
        try {

            FFmpegFrameGrabber fFmpegFrameGrabber = new FFmpegFrameGrabber(videoPath.concat(videoFileName));
            fFmpegFrameGrabber.start();
            //获取视频总帧数
            int ftp = fFmpegFrameGrabber.getLengthInFrames();
            String rotate = fFmpegFrameGrabber.getVideoMetadata("rotate");
            while (flag <= ftp) {
                frame = fFmpegFrameGrabber.grabImage();
                opencv_core.IplImage src = null;

                if (frame != null && flag==1) {
                    // 视频旋转调整
                    if(StringUtils.isNotBlank(rotate)){
                        OpenCVFrameConverter.ToIplImage converter =new OpenCVFrameConverter.ToIplImage();
                        src =converter.convert(frame);
                        frame =converter.convert(rotate(src, Integer.valueOf(rotate)));
                    }

                    //文件储存对象
                    File outPut = new File(videoPath+videoFileName);

                    BufferedImage bufferedImage = FrameToBufferedImage(frame);
                    int width = bufferedImage.getWidth();
                    int height = bufferedImage.getHeight();

                    // 图片大小限制
                    if(width > height){
                        // 高宽比
                        double proportion = (double)height/(double)width;
                        width = 600;
                        BigDecimal bd=new BigDecimal(width * proportion).setScale(0, BigDecimal.ROUND_HALF_UP);
                        height = bd.intValue();
                    }
                    // 图片压缩
                    BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
                    bi.getGraphics().drawImage(bufferedImage.getScaledInstance(width, height, Image.SCALE_SMOOTH),
                            0, 0, null);
                    ImageIO.write(bi, "jpg", outPut);
                    break;
                }
                flag++;
            }

            fFmpegFrameGrabber.stop();
            fFmpegFrameGrabber.close();
        } catch (Exception e) {
           e.printStackTrace();
        }
        return pivStr;
    }

    public static BufferedImage FrameToBufferedImage(Frame frame) {
        //创建BufferedImage对象
        Java2DFrameConverter converter = new Java2DFrameConverter();
        BufferedImage bufferedImage = converter.getBufferedImage(frame);
        return bufferedImage;
    }

    public static opencv_core.IplImage rotate(opencv_core.IplImage src, int rotate) {
        opencv_core.IplImage img = opencv_core.IplImage.create(src.height(), src.width(), src.depth(), src.nChannels());
        opencv_core.cvTranspose(src, img);
        opencv_core.cvFlip(img, img, rotate);
        return img;
    }*/


}
