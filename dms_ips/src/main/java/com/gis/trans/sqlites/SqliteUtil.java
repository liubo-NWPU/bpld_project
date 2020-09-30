package com.gis.trans.sqlites;

import com.gis.trans.model.ShapePoint;
import com.gis.trans.sqlites.SqliteConfig;
import com.gis.trans.sqlites.SqliteGenerator;
import com.gis.trans.sqlites.database.sql.SqliteDBLocalFactory;
import com.gis.trans.tiles3d.generator.PntcGenerationException;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SqliteUtil {

    //创建sqlite数据数据库文件，保存所有点信息，一个diffimage对应一个文件
    public static boolean createSqliteDatabase(List<ShapePoint> plotList, String sqlFilePath, String radarKey){
        SqliteConfig config = new SqliteConfig();
        config.setFilePath(sqlFilePath);
        File path = new File(sqlFilePath);
        if (!path.exists()){
            path.mkdirs();
        }

        boolean success = false;
        SqliteDBLocalFactory dblocalManagerFactory = new SqliteDBLocalFactory(config);
        SqliteGenerator generator = new SqliteGenerator(config, dblocalManagerFactory);
        try {
            generator.createsqlite(plotList,radarKey);
        } catch (PntcGenerationException e) {
            com.gis.trans.tiles3d.util.Logger.error(e.getMessage());
            Throwable cause = e.getCause();
            while (cause != null) {
                com.gis.trans.tiles3d.util.Logger.error("Cause: " + cause.getMessage());
                cause = cause.getCause();
            }
        }
        return success;
    }

    public synchronized static List<ShapePoint> readPointList(String sqlFilePath) throws PntcGenerationException {
        List<ShapePoint> list = new ArrayList<>();
        SqliteConfig config = new SqliteConfig();
        config.setFilePath(sqlFilePath);
        File path = new File(sqlFilePath);
        if (!path.exists()){
            throw new PntcGenerationException("sqlite数据文件不存在‘"+sqlFilePath+"’");
        }

        SqliteDBLocalFactory dblocalManagerFactory = new SqliteDBLocalFactory(config);
        SqliteGenerator generator = new SqliteGenerator(config, dblocalManagerFactory);
        try {
            list = generator.readPointList();
        } catch (PntcGenerationException e) {
            e.printStackTrace();
            return null;
        }
        if(list == null){
            System.out.println("读取数据失败");
        }
        return list;
    }
}
