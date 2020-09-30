package com.gis.trans.dao;

import com.gis.trans.model.RainInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RainInfoDao extends JpaRepository<RainInfo,Integer>,JpaSpecificationExecutor {

}
