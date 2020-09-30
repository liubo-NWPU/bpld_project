package com.gis.trans.service.impl;

import com.gis.trans.constant.CRUD;
import com.gis.trans.dao.RainDataDao;
import com.gis.trans.model.RainData;
import com.gis.trans.model.RainInfo;
import com.gis.trans.model.ResponseModel;
import com.gis.trans.service.RainDataService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Service
public class RainDataServiceImpl extends BaseServiceImpl<RainData, Long> implements RainDataService {

    @Autowired
    private RainDataDao rainDataDao;


    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public JpaRepository<RainData, Long> getDao() {
        return rainDataDao;
    }

    @Override
    public ResponseModel searchAll() {
        ResponseModel responseModel = new ResponseModel();
        try {
            responseModel.setData(queryAll());
            responseModel.setSuccess(true);
            responseModel.setMessage(CRUD.SEARCH_SUCCESS);
        }catch (Exception e){
            responseModel.setSuccess(false);
            responseModel.setMessage(CRUD.SEARCH_FAIL);
            e.printStackTrace();
        }
        return responseModel;
    }

    @Override
    public ResponseModel searchByRDAddr(Integer rDAddr) {
        ResponseModel responseModel = new ResponseModel();
        try {
            responseModel.setData(rainDataDao.findByRDAddr(rDAddr));
            responseModel.setSuccess(true);
            responseModel.setMessage(CRUD.SEARCH_SUCCESS);
        }catch (Exception e){
            responseModel.setSuccess(false);
            responseModel.setMessage(CRUD.SEARCH_FAIL);
            e.printStackTrace();
        }
        return responseModel;
    }


    @Override
    public ResponseModel searchByDate(Integer rDAddr ,String startTime, String endTime) {
        ResponseModel responseModel = new ResponseModel();
        List<RainData> list = null;
        try {
            /*Specification<RainData> radarsBRSpecification = new Specification<RainData>() {
                @Override
                public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    List<Predicate> predicates = new ArrayList<>();
                    if(!StringUtils.isEmpty(startTime)){
                        predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("rDDate").as(String.class),startTime));
                    }
                    if(!StringUtils.isEmpty(endTime)){
                        predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("rDDate").as(String.class),endTime));
                    }
                    predicates.add(criteriaBuilder.equal(root.get("rDAddr").as(Integer.class),rDAddr));
                    return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
                }
            };
            list = rainDataDao.findAll(radarsBRSpecification);
            list.sort(new Comparator<RainData>() {
                @Override
                public int compare(RainData o1, RainData o2) {
                    if (o1.getrDDate().getTime() > o2.getrDDate().getTime()) {
                        return 1;
                    }
                    if (o1.getrDDate().getTime() < o2.getrDDate().getTime()) {
                        return -1;
                    }
                    return 0;
                }
            });*/
            String baseSql = "select r from RainData r where 1=1 ";
            StringBuilder stringBuilder = new StringBuilder(baseSql);
            if (rDAddr!=null) {
                stringBuilder.append(String.format(" and r.rDAddr = '%d' ",rDAddr));
            }
            if (!StringUtils.isEmpty(startTime)) {
                stringBuilder.append(String.format(" and r.rDDate >= '%s' ",startTime));
            }
            if (!StringUtils.isEmpty(endTime)) {
                stringBuilder.append(String.format(" and r.rDDate <= '%s' ",endTime));
            }
            stringBuilder.append(" order by r.rDDate ");
            TypedQuery<RainData> query = entityManager.createQuery(stringBuilder.toString(),RainData.class);
            list = query.getResultList();
            responseModel.setData(list);
            responseModel.setSuccess(true);
            responseModel.setMessage(CRUD.SEARCH_SUCCESS);
        }catch (Exception e){
            responseModel.setSuccess(false);
            responseModel.setException(e.getClass().getName());
            responseModel.setMessage(CRUD.SEARCH_FAIL);
            e.printStackTrace();
        }
        return responseModel;
    }

    @Override
    public  List<RainData> rainDataExportExcel(String rDAddr, String startTime, String endTime) {
        List<RainData> list = null;
        try {
            String baseSql = "select r from RainData r where 1=1 ";
            StringBuilder stringBuilder = new StringBuilder(baseSql);
            if (!StringUtils.isEmpty(rDAddr)) {
                stringBuilder.append(String.format(" and r.rDAddr = '%s' ",rDAddr));
            }
            if (!StringUtils.isEmpty(startTime)) {
                stringBuilder.append(String.format(" and r.rDDate >= '%s' ",startTime));
            }
            if (!StringUtils.isEmpty(endTime)) {
                stringBuilder.append(String.format(" and r.rDDate <= '%s' ",endTime));
            }
            stringBuilder.append(" order by r.rDDate ");
            TypedQuery<RainData> query = entityManager.createQuery(stringBuilder.toString(),RainData.class);
            list = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
