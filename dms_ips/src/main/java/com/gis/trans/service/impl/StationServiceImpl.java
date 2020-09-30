//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.gis.trans.service.impl;

import com.gis.trans.dao.StationDao;
import com.gis.trans.model.ResponseModel;
import com.gis.trans.model.Station;
import com.gis.trans.model.StationNew;
import com.gis.trans.service.StationNewService;
import com.gis.trans.service.StationService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class StationServiceImpl extends BaseServiceImpl<Station, String> implements StationService {

    @Resource
    private StationDao stationDao;

    @Resource
    private StationNewService stationNewService;


    public JpaRepository<Station, String> getDao() {
        return this.stationDao;
    }

    public ResponseModel searchAll() {
        ResponseModel responseModel = new ResponseModel();
        try {
            List<Station> stations = this.queryAll();
            List<StationNew> stationNews = stationNewService.searchAll();
            if (stationNews!=null){
                for (StationNew stationNew : stationNews) {
                    Station station = new Station();
                    BeanUtils.copyProperties(stationNew,station);
                    stations.add(station);
                }
            }
            responseModel.setData(stations);
            responseModel.setSuccess(true);
            responseModel.setMessage("查询成功");
        } catch (Exception var3) {
            responseModel.setSuccess(false);
            responseModel.setMessage("查询失败");
            var3.printStackTrace();
        }

        return responseModel;
    }

    @Override
    public List<Station> findByMStationName(String stationName) {
        return stationDao.findByMStationName(stationName);
    }
}
