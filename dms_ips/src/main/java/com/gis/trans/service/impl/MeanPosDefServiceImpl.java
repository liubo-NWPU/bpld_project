//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.gis.trans.service.impl;

import com.gis.trans.dao.MeanPosDefDao;
import com.gis.trans.model.*;
import com.gis.trans.service.MeanPosDefNewService;
import com.gis.trans.service.MeanPosDefService;
import com.gis.trans.service.StationNewService;
import com.gis.trans.service.StationService;
import com.gis.trans.utils.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class MeanPosDefServiceImpl extends BaseServiceImpl<MeanPosDef, Long> implements MeanPosDefService {
    @Autowired
    private MeanPosDefDao meanPosDefDao;

    @Resource
    private MeanPosDefNewService meanPosDefNewService;
    @Resource
    private StationService stationService;
    @Resource
    private StationNewService stationNewService;

    public JpaRepository<MeanPosDef, Long> getDao() {
        return this.meanPosDefDao;
    }

    public ResponseModel searchAll() {
        ResponseModel responseModel = new ResponseModel();
        try {
            responseModel.setData(this.queryAll());
            responseModel.setSuccess(true);
            responseModel.setMessage("查询成功");
        } catch (Exception var3) {
            responseModel.setSuccess(false);
            responseModel.setMessage("查询失败");
            var3.printStackTrace();
        }

        return responseModel;
    }

    public ResponseModel searchByStationId(String stationName) {
        ResponseModel responseModel = new ResponseModel();
        try {
            List<StationNew> stationNews = stationNewService.findByMStationName(stationName);
            if (stationNews != null && stationNews.size() > 0) {
                Integer id = stationNews.get(0).getmStationId();
                List<MeanPosDefNew> meanPosDefNews = meanPosDefNewService.searchByStationId(id);
                if (meanPosDefNews!=null&&meanPosDefNews.size()>0){
                    responseModel.setData(meanPosDefNews.get(0));
                }
            } else {
                List<Station> stations = stationService.findByMStationName(stationName);
                if (stations != null && stations.size() > 0) {
                    Integer id = stations.get(0).getmStationId();
                    List<MeanPosDef> meanlists = meanPosDefDao.findByStationId(id);
                    if (meanlists!=null&&meanlists.size()>0){
                        responseModel.setData(meanlists.get(0));
                    }
                }
            }
            responseModel.setSuccess(true);
            responseModel.setMessage("查询成功");
        } catch (Exception var4) {
            responseModel.setSuccess(false);
            responseModel.setMessage("查询失败");
            var4.printStackTrace();
        }

        return responseModel;
    }

    @Override
    public ResponseModel searchByDate(String stationName, String startTime, String endTime) {
        ResponseModel responseModel = new ResponseModel();
        List list = new ArrayList();
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
            List<StationNew> stationNews = stationNewService.findByMStationName(stationName);
            if (stationNews != null && stationNews.size() > 0) {
                Integer id = stationNews.get(0).getmStationId();
                list = meanPosDefNewService.searchByDate(id, startTime, endTime);

            } else {
                List<Station> stations = stationService.findByMStationName(stationName);
                if (stations != null && stations.size() > 0) {
                    Integer id = stations.get(0).getmStationId();
                    list = this.meanPosDefDao.findByStationIdAndTime(id, DateUtils.getNetTime(simpleDateFormat.parse(startTime).getTime()), DateUtils.getNetTime(simpleDateFormat.parse(endTime).getTime()));
                }
            }
            responseModel.setData(list);
            responseModel.setSuccess(true);
            responseModel.setMessage("查询成功");
        } catch (Exception var8) {
            responseModel.setSuccess(false);
            responseModel.setException(var8.getClass().getName());
            responseModel.setMessage("查询失败");
            var8.printStackTrace();
        }

        return responseModel;
    }

    @Override
    public List<MeanPosDefVo> exportExcel(String stationName, String startTime, String endTime) {

        List<MeanPosDefVo>  list = new ArrayList();
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
            List<StationNew> stationNews = stationNewService.findByMStationName(stationName);
            if (stationNews != null && stationNews.size() > 0) {
                Integer id = stationNews.get(0).getmStationId();
                List<MeanPosDefNew>   list1 = meanPosDefNewService.searchByDate(id, startTime, endTime);
                if(list1!=null && list1.size()>0){
                    for(MeanPosDefNew meanPosDefNew:list1){
                        MeanPosDefVo obj=new MeanPosDefVo();
                        BeanUtils.copyProperties(meanPosDefNew,obj);
                        list.add(obj);
                    }
                }

            } else {
                List<Station> stations = stationService.findByMStationName(stationName);
                if (stations != null && stations.size() > 0) {
                    Integer id = stations.get(0).getmStationId();
                    List<MeanPosDef> list2 = this.meanPosDefDao.findByStationIdAndTime(id, DateUtils.getNetTime(simpleDateFormat.parse(startTime).getTime()), DateUtils.getNetTime(simpleDateFormat.parse(endTime).getTime()));
                    if (list2 != null && list2.size() > 0) {
                        for (MeanPosDef meanPosDef : list2) {
                            MeanPosDefVo obj = new MeanPosDefVo();
                            BeanUtils.copyProperties(meanPosDef, obj);
                            list.add(obj);
                        }
                    }
                }
            }
        } catch (Exception var8) {
            var8.printStackTrace();
        }
        return list;
    }


}
