package com.geovis.web.domain.system;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;

/**
 * Created by Administrator on 2018/12/7.
 */
@Table(name="sys_file")
public class SysFile {

    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)//通过在TkMapperConfig生成策略
    private String id;

    @Column(name = "path")
    private String path;
}
