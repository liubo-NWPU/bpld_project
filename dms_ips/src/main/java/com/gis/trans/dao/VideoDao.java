package com.gis.trans.dao;

import com.gis.trans.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(rollbackFor = Exception.class)
@Repository
public interface VideoDao extends JpaRepository<Video,Long> {

    List<Video> findByName(String VideoName);

    Video findById(Long VideoId);

}
