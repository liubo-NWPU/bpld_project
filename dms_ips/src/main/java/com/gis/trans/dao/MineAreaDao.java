package com.gis.trans.dao;

import com.gis.trans.model.MineArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(rollbackFor = Exception.class)
@Repository
public interface MineAreaDao  extends JpaRepository<MineArea,Long> {

    List<MineArea> findByMineName(String name);

    MineArea findByMineId(Long mineId);

}
