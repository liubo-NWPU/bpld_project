package com.gis.trans.dao;

import com.gis.trans.model.Tesaaa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(rollbackFor = Exception.class)
@Repository
public interface TesaaaDao extends JpaRepository<Tesaaa,Long> ,JpaSpecificationExecutor {

}
