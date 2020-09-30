//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.gis.trans.service.impl;

import com.gis.trans.dao.MeanPosDefNewDao;
import com.gis.trans.model.MeanPosDefNew;
import com.gis.trans.model.ResponseModel;
import com.gis.trans.service.MeanPosDefNewService;
import com.gis.trans.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class MeanPosDefNewServiceImpl extends BaseServiceImpl<MeanPosDefNew, Long> implements MeanPosDefNewService {
    @Autowired
    private MeanPosDefNewDao meanPosDefNewDao;

    public MeanPosDefNewServiceImpl() {
    }

    public JpaRepository<MeanPosDefNew, Long> getDao() {
        return this.meanPosDefNewDao;
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

    public List<MeanPosDefNew> searchByStationId(Integer id) {
       return  meanPosDefNewDao.findByStationId(id);
    }

    public List<MeanPosDefNew> searchByDate(Integer id, String startTime, String endTime) {
        List<MeanPosDefNew> list = null;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
            list = this.meanPosDefNewDao.findByStationIdAndTime(id, DateUtils.getNetTime(simpleDateFormat.parse(startTime).getTime()), DateUtils.getNetTime(simpleDateFormat.parse(endTime).getTime()));
        } catch (Exception var8) {
            var8.printStackTrace();
        }

        return list;
    }
}
