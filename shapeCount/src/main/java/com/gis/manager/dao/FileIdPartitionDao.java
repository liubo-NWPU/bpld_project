package com.gis.manager.dao;

import com.gis.manager.model.FileIdPartition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * @Author:liuyan
 * @Date:创建于2019/12/18 17:01
 * @Description:
 */
@Repository
public interface FileIdPartitionDao extends JpaRepository<FileIdPartition,Long> {

//    FileIdPartition findByFileId(Long fileId);

}
