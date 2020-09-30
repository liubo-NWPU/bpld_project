package com.gis.trans.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FileConfig {

    @Value("${file.ply.filepath}")
    private String plyfile;
    @Value("${file.ply.exists}")
    private boolean plyexists;
    @Value("${file.sqlite.filepath}")
    private String sqlitefile;
    @Value("${file.3dtiles.filepath}")
    private String tilesfile;

    public String getPlyfile() {
        return plyfile;
    }

    public void setPlyfile(String plyfile) {
        this.plyfile = plyfile;
    }

    public boolean isPlyexists() {
        return plyexists;
    }

    public void setPlyexists(boolean plyexists) {
        this.plyexists = plyexists;
    }

    public String getSqlitefile() {
        return sqlitefile;
    }

    public void setSqlitefile(String sqlitefile) {
        this.sqlitefile = sqlitefile;
    }

    public String getTilesfile() {
        return tilesfile;
    }

    public void setTilesfile(String tilesfile) {
        this.tilesfile = tilesfile;
    }
}
