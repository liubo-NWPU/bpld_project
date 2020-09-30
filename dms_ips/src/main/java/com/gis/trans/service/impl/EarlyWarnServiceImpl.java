package com.gis.trans.service.impl;

import com.gis.trans.constant.CRUD;
import com.gis.trans.dao.EarlyWarnDao;
import com.gis.trans.model.EarlyWarn;
import com.gis.trans.model.ResponseModel;
import com.gis.trans.service.EarlyWarnService;
import com.gis.trans.utils.HttpClientUtil;
import com.gis.trans.utils.MrpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

@Service
public class EarlyWarnServiceImpl extends BaseServiceImpl<EarlyWarn, Long> implements EarlyWarnService {


    @Value("${auth.local_url}")
    private String authUrl;

    @Autowired
    private EarlyWarnDao earlyWarnDao;


    @Override
    public JpaRepository<EarlyWarn, Long> getDao() {
        return earlyWarnDao;
    }

    @Override
    public ResponseModel saveInf(EarlyWarn earlyWarn,String userId) {
        ResponseModel responseModel = new ResponseModel();
        List<Long> mrpList = MrpUtils.getMrp(authUrl,userId);
        if (!mrpList.contains(earlyWarn.getRadarId())){
            responseModel.setSuccess(false);
            responseModel.setMessage("该用户无设置权限");
            return responseModel;
        }
        HashMap<Object, Object> map2 = new HashMap<>();
        String post1 = HttpClientUtil.get(authUrl + "system/users/mrp" + earlyWarn.getRadarId(), map2);
        try {
            EarlyWarn earlyWarn1 = earlyWarnDao.findByRadarId(earlyWarn.getRadarId());
            if (earlyWarn1 != null) {
                responseModel.setSuccess(false);
                responseModel.setMessage("该雷达已设置预警信息");
                return responseModel;
            }
            earlyWarn.setUpdatetime(new Date());
            earlyWarnDao.save(earlyWarn);
            responseModel.setSuccess(true);
            responseModel.setMessage(CRUD.SAVE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            responseModel.setSuccess(false);
            responseModel.setMessage(CRUD.SAVE_FAIL);
        }
        return responseModel;
    }

    @Override
    public ResponseModel deleteOne(Long id) {
        ResponseModel responseModel = new ResponseModel();
        try {
            earlyWarnDao.delete(id);
            responseModel.setSuccess(true);
            responseModel.setMessage(CRUD.DELETE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            responseModel.setSuccess(false);
            responseModel.setMessage(CRUD.DELETE_FAIL);
        }
        return responseModel;
    }

    @Override
    public EarlyWarn selectByRadarId(Long radarId) {
        ResponseModel responseModel = new ResponseModel();
        EarlyWarn earlyWarn = new EarlyWarn();
        try {
            earlyWarn = earlyWarnDao.findByRadarId(radarId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return earlyWarn;
    }

    @Override
    public List<EarlyWarn> selectListByUserId(String userId) {
        List<EarlyWarn> earlyWarns = queryAll();
        if (userId == null) {
            return earlyWarns;
        } else {
            if (earlyWarns.size() > 0) {
                List<Long> mrpList = MrpUtils.getMrp(authUrl,userId);
                Iterator<EarlyWarn> iterator = earlyWarns.iterator();
                while (iterator.hasNext()){
                    EarlyWarn next = iterator.next();
                    if (!mrpList.contains(next.getRadarId())) {
                        iterator.remove();
                    }
                }
            }
            return earlyWarns;
        }
    }
}
