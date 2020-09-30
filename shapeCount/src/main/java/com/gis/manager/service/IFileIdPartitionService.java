package com.gis.manager.service;

import com.gis.manager.model.FileIdPartition;

/**
 * @Author:liuyan
 * @Date:创建于2019/12/18 17:01
 * @Description:
 */
public interface IFileIdPartitionService extends IBaseService<FileIdPartition,Long> {

    void save(FileIdPartition fileIdPartition);
}
