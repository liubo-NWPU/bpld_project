//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.gis.trans.service.impl;

import com.gis.trans.dao.StationNewDao;
import com.gis.trans.model.StationNew;
import com.gis.trans.service.StationNewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StationNewServiceImpl extends BaseServiceImpl<StationNew, String> implements StationNewService {
    @Autowired
    private StationNewDao stationNewDao;

    public StationNewServiceImpl() {
    }

    public JpaRepository<StationNew, String> getDao() {
        return this.stationNewDao;
    }

    public List<StationNew> searchAll() {
        return this.queryAll();
    }

    @Override
    public List<StationNew> findByMStationName(String stationName) {
        return stationNewDao.findByMStationName(stationName);
    }
}
