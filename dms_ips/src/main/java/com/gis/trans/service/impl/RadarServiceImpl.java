package com.gis.trans.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.gis.trans.constant.CRUD;
import com.gis.trans.dao.RadarDao;
import com.gis.trans.model.MineArea;
import com.gis.trans.model.Radar;
import com.gis.trans.model.ResponseModel;
import com.gis.trans.service.MineAreaService;
import com.gis.trans.service.RadarService;
import com.gis.trans.utils.HttpUtil;
import com.gis.trans.utils.MrpUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.*;

@Service
public class RadarServiceImpl extends BaseServiceImpl<Radar, Long> implements RadarService {
    @Autowired
    private RadarDao radarDao;

    @Autowired
    private MineAreaService mineAreaService;

    @Value("${system.file.manager}")
    private String managerUrl;


    @Value("${auth.local_url}")
    private String authUrl;

    @Autowired
    private EntityManager entityManager;

    @Override
    public JpaRepository<Radar, Long> getDao() {
        return radarDao;
    }

    @Override
    public ResponseModel addRadar(Radar radar,String userId) {
        ResponseModel responseModel = new ResponseModel();
//        List<Long> mrpList = MrpUtils.getMrp(authUrl,userId);
//        if (!mrpList.contains(radar.getRadarId())){
//            responseModel.setSuccess(false);
//            responseModel.setMessage("该用户无设置权限");
//            return null;
//        }
        if (!StringUtils.isEmpty(radar.getRadarName())){
            radar.setRadarKey(radar.getRadarName().toLowerCase());
        }
        try {
            Map map = new HashMap();
            map.put("id", radar.getRadarId().toString());
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
            map1.put("id", radar.getRadarId().toString());
            String s1 = HttpUtil.sendGet(managerUrl+"/manager/rest/filecatalog/getParentById", map1);
            if (s1==null){ //表示在一级目录下
                responseModel.setSuccess(false);
                responseModel.setMessage("非矿场文件夹下禁止添加雷达信息");
                return responseModel;
            }else {
                Long parentId = JSONObject.parseObject(s1).getLong("id");
                MineArea mineArea = mineAreaService.searchByMineId(parentId,userId);
                if (mineArea==null){
                    responseModel.setSuccess(false);
                    responseModel.setMessage("非矿场文件夹下禁止添加雷达信息");
                    return responseModel;
                }
            }
            MineArea mineArea = mineAreaService.searchByMineId(radar.getRadarId(),userId);
            if (mineArea!=null){
                responseModel.setSuccess(false);
                responseModel.setMessage("该文件夹已添加矿场信息");
                return responseModel;
            }
            radarDao.save(radar);
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
    public ResponseModel deleteAllRadar(Long[] ids) {
        ResponseModel responseModel = new ResponseModel();
        try {
            for (Long id : ids) {
                deleteRadar(id);
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
    public ResponseModel deleteRadar(Long id) {
        ResponseModel responseModel = new ResponseModel();
        try {
            Radar radar = radarDao.findByRadarId(id);
            if (radar != null) {
                delete(radar);
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
    public List<Radar> searchByName(String name,String userId) {
        List<Radar> radars = radarDao.findByRadarNameLike("%"+name+"%");
        if (radars.size() > 0) {
            List<Long> mrpList = MrpUtils.getMrp(authUrl,userId);
            Iterator<Radar> iterator = radars.iterator();
            while (iterator.hasNext()){
                Radar next = iterator.next();
                if (!mrpList.contains(next.getRadarId())) {
                    iterator.remove();
                }
            }
        }
        return radars;
    }
    @Override
    public Radar checkByName(String name) {
        Radar radar = radarDao.findByRadarName(name);
        return radar;
    }

    @Override
    public Radar searchByRadarId(Long radarId,String userId) {
        List<Long> mrpList = MrpUtils.getMrp(authUrl,userId);
        if (!mrpList.contains(radarId)){
            return null;
        }

        Radar radar = radarDao.findByRadarId(radarId);
        return radar;
    }

    @Override
    public Radar findByRadarKey(String radarKey) {
       return radarDao.findByRadarKey(radarKey);
    }

    @Override
    public Radar findByRadarName(String radarName) {
        return radarDao.findByRadarName(radarName);
    }

    @Override
    public ResponseModel search(Radar radar, String userId) {
        ResponseModel responseModel = new ResponseModel();

        List<MineArea> list=new ArrayList<>();
        StringBuilder sql = new StringBuilder("select * from radar where 1=1");
        if (radar != null && radar.getRadarId() != null) {
            sql.append(" and radar_id = '" + radar.getRadarId() + "'");
        }

        if (radar != null && radar.getRadarName() != null) {
            sql.append(" and radar_name like '%" + radar.getRadarName() + "%'");
        }

        if (radar != null && radar.getMineAreaid() != null) {
            sql.append(" and mine_area_id = '" + radar.getMineAreaid() + "'");
        }
        if (radar != null && radar.getMineName() != null) {
            sql.append(" and mine_name like '%" +  radar.getMineName() + "%'");
        }

        Query query = entityManager.createNativeQuery(sql.toString(), Radar.class);
        list = query.getResultList();
        responseModel.setData(list);
        responseModel.setSuccess(true);
        responseModel.setMessage("查询成功");
        return responseModel;
    }

}
