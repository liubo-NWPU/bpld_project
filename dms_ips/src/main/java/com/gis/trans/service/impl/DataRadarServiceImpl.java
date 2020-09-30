package com.gis.trans.service.impl;

import com.gis.trans.constant.CRUD;
import com.gis.trans.dao.DataRadarDao;
import com.gis.trans.model.DataRadar;
import com.gis.trans.model.ResponseModel;
import com.gis.trans.service.DataRadarService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Service
public class DataRadarServiceImpl extends BaseServiceImpl<DataRadar, Long> implements DataRadarService {

    @Autowired
    private DataRadarDao dataRadarDao;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public JpaRepository<DataRadar, Long> getDao() {
        return dataRadarDao;
    }

    @Override
    public ResponseModel searchAll() {
        ResponseModel responseModel = new ResponseModel();
        try {
            responseModel.setData(queryAll());
            responseModel.setSuccess(true);
            responseModel.setMessage(CRUD.SEARCH_SUCCESS);
        } catch (Exception e) {
            responseModel.setSuccess(false);
            responseModel.setMessage(CRUD.SEARCH_FAIL);
            e.printStackTrace();
        }
        return responseModel;
    }

    @Override
    public ResponseModel searchByDeviceId(String id) {
        ResponseModel responseModel = new ResponseModel();
        try {
            responseModel.setData(dataRadarDao.findByDeviceId(id));
            responseModel.setSuccess(true);
            responseModel.setMessage(CRUD.SEARCH_SUCCESS);
        } catch (Exception e) {
            responseModel.setSuccess(false);
            responseModel.setMessage(CRUD.SEARCH_FAIL);
            e.printStackTrace();
        }
        return responseModel;
    }


    @Override
    public ResponseModel searchByDate(String id, String startTime, String endTime) {
        ResponseModel responseModel = new ResponseModel();
        List<DataRadar> list = null;
        try {
/*            Specification<DataRadar> radarsBRSpecification = new Specification<DataRadar>() {
                @Override
                public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    List<Predicate> predicates = new ArrayList<>();
                    if (!StringUtils.isEmpty(startTime)) {
                        predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("time").as(String.class), startTime));
                    }
                    if (!StringUtils.isEmpty(endTime)) {
                        predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("time").as(String.class), endTime));
                    }
                    predicates.add(criteriaBuilder.equal(root.get("deviceId").as(String.class), id));
                    return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
                }
            };

             list = dataRadarDao.findAll(radarsBRSpecification);

            list.sort(new Comparator<DataRadar>() {
                @Override
                public int compare(DataRadar o1, DataRadar o2) {
                    if (o1.getTime().getTime() > o2.getTime().getTime()) {
                        return 1;
                    }
                    if (o1.getTime().getTime() < o2.getTime().getTime()) {
                        return -1;
                    }
                    return 0;
                }
            });

            */
            String baseSql = "select d from DataRadar d where 1=1 ";
            StringBuilder stringBuilder = new StringBuilder(baseSql);
            if (!StringUtils.isEmpty(id)) {
                 stringBuilder.append(String.format(" and d.deviceId = '%s' ",id));
            }
            if (!StringUtils.isEmpty(startTime)) {
                stringBuilder.append(String.format(" and d.time >= '%s' ",startTime));
            }
            if (!StringUtils.isEmpty(endTime)) {
                stringBuilder.append(String.format(" and d.time <= '%s' ",endTime));
            }
            stringBuilder.append(" order by d.time");
            TypedQuery<DataRadar> query = entityManager.createQuery(stringBuilder.toString(),DataRadar.class);
            list = query.getResultList();
            responseModel.setData(list);
            responseModel.setSuccess(true);
            responseModel.setMessage(CRUD.SEARCH_SUCCESS);
        } catch (Exception e) {
            responseModel.setSuccess(false);
            responseModel.setException(e.getClass().getName());
            responseModel.setMessage(CRUD.SEARCH_FAIL);
            e.printStackTrace();
        }
        return responseModel;
    }

}
