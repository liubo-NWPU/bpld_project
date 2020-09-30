package com.gis.trans.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.gis.trans.constant.CRUD;
import com.gis.trans.dao.MineAreaDao;
import com.gis.trans.model.MineArea;
import com.gis.trans.model.Radar;
import com.gis.trans.model.ResponseModel;
import com.gis.trans.service.MineAreaService;
import com.gis.trans.service.RadarService;
import com.gis.trans.utils.HttpUtil;
import com.gis.trans.utils.MrpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.*;

@Service
public class MineAreaServiceImpl extends BaseServiceImpl<MineArea, Long> implements MineAreaService {

    @Autowired
    private MineAreaDao mineAreaDao;

    @Autowired
    private RadarService radarService;

    @Value("${system.file.manager}")
    private String managerUrl;


    @Value("${auth.local_url}")
    private String authUrl;
    @Autowired
    private EntityManager entityManager;

    @Override
    public JpaRepository<MineArea, Long> getDao() {
        return mineAreaDao;
    }

    @Override
    public ResponseModel addMineArea(MineArea mineArea,String userId) {
        ResponseModel responseModel = new ResponseModel();
        List<Long> mrpList = MrpUtils.getMrp(authUrl,userId);
        if (!mrpList.contains(mineArea.getMineId())){
            responseModel.setSuccess(false);
            responseModel.setMessage("该用户无设置权限");
            return responseModel;
        }

        try {
            Map map = new HashMap();
            map.put("id", mineArea.getMineId().toString());
            String s = HttpUtil.sendGet(managerUrl+"/manager/rest/filecatalog/getById", map);
            if (s!=null){
                Short fileType = JSONObject.parseObject(s).getShort("fileType");
                if (fileType!=0){
                    responseModel.setSuccess(false);
                    responseModel.setMessage("非文件夹禁止添加矿场信息");
                    return responseModel;
                }
            }

            Radar radar = radarService.searchByRadarId(mineArea.getMineId(),userId);
            if (radar!=null){
                responseModel.setSuccess(false);
                responseModel.setMessage("该文件夹已添加雷达信息");
                return responseModel;
            }
            mineAreaDao.save(mineArea);
            insert(mineArea);
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
    public ResponseModel deleteAllMineArea(Long[] ids) {
        ResponseModel responseModel = new ResponseModel();
        try {
            for (Long id : ids) {
                deleteMineArea(id);
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
    public ResponseModel deleteMineArea(Long mineId) {
        ResponseModel responseModel = new ResponseModel();
        try {
            MineArea mineArea = mineAreaDao.findByMineId(mineId);
            if (mineArea != null) {
                delete(mineArea);
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
    public List<MineArea> searchByName(String name,String userId) {
        List<MineArea> mineAreas = mineAreaDao.findByMineName(name);
        if (mineAreas.size() > 0) {
            List<Long> mrpList = MrpUtils.getMrp(authUrl,userId);
            Iterator<MineArea> iterator = mineAreas.iterator();
            while (iterator.hasNext()){
                MineArea next = iterator.next();
                if (!mrpList.contains(next.getMineId())) {
                    iterator.remove();
                }
            }
        }
        return mineAreas;
    }

    @Override
    public MineArea searchByMineId(Long mineId,String userId) {
        ResponseModel responseModel = new ResponseModel();
        List<Long> mrpList = MrpUtils.getMrp(authUrl,userId);
        if (!mrpList.contains(mineId)){
            responseModel.setSuccess(false);
            responseModel.setMessage("该用户无设置权限");
            return null;
        }
        return mineAreaDao.findByMineId(mineId);
    }

    @Override
    public ResponseModel search(MineArea mineArea, String userId) {
        ResponseModel responseModel = new ResponseModel();
        List<MineArea> list=new ArrayList<>();
        StringBuilder sql = new StringBuilder("select * from mine_area where 1=1");
        if (mineArea != null && mineArea.getMineId() != null) {
            sql.append(" and mine_id = '" + mineArea.getMineId() + "'");
        }

        if (mineArea != null && mineArea.getCompanyName() != null) {
            sql.append(" and company_name like '%" + mineArea.getCompanyName() + "%'");
        }

        if (mineArea != null && mineArea.getMineName() != null) {
            sql.append(" and mine_name like '%" + mineArea.getMineName() + "%'");
        }
        if (mineArea != null && mineArea.getLocation() != null) {
            sql.append(" and location like '%" +  mineArea.getLocation() + "%'");
        }

        Query query = entityManager.createNativeQuery(sql.toString(), MineArea.class);
        list = query.getResultList();
        responseModel.setData(list);
        responseModel.setSuccess(true);
        responseModel.setMessage("查询成功");
        return responseModel;
    }
}
