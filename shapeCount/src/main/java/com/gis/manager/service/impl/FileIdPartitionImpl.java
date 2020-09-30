package com.gis.manager.service.impl;

import com.gis.manager.dao.FileIdPartitionDao;
import com.gis.manager.model.FileIdPartition;
import com.gis.manager.service.IFileIdPartitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

/**
 * @Author:liuyan
 * @Date:创建于2019/12/21 17:03
 * @Description:
 */
@Service
public class FileIdPartitionImpl extends BaseServiceImpl<FileIdPartition, Long> implements IFileIdPartitionService {

    @Autowired
    private FileIdPartitionDao fileIdPartitionDao;

    @Override
    public JpaRepository<FileIdPartition, Long> getDao() {
        return fileIdPartitionDao;
    }


    @Override
    public void save(FileIdPartition fileIdPartition) {
        fileIdPartitionDao.save(fileIdPartition);
    }

//    @Override
//    public FileIdPartition findByFileId(Long fileId) {
//        return fileIdPartitionDao.findByFileId(fileId);
//    }
}
