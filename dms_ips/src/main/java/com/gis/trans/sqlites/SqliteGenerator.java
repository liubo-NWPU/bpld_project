/*
 * Cesium Point Cloud Generator
 * 
 * Copyright 2017 - 2018
 * Chair of Geoinformatics
 * Technical University of Munich, Germany
 * https://www.gis.bgu.tum.de/
 * 
 * The Cesium Point Cloud Generator is developed at Chair of Geoinformatics,
 * Technical University of Munich, Germany.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gis.trans.sqlites;

import com.gis.trans.model.ShapePoint;
import com.gis.trans.sqlites.database.DBLocalManager;
import com.gis.trans.sqlites.database.DBLocalManagerFactory;
import com.gis.trans.tiles3d.concurrent.PntcTileWork;
import com.gis.trans.tiles3d.database.DBManagerFactory;
import com.gis.trans.tiles3d.database.sqlite.SqliteDBManager;
import com.gis.trans.tiles3d.generator.PntcGenerationException;
import com.gis.trans.tiles3d.model.PointObject;
import com.gis.trans.tiles3d.util.Logger;
import org.citydb.api.concurrent.WorkerPool;

import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SqliteGenerator {
	final static String ENCODING = "UTF-8";
	final static Charset CHARSET = Charset.forName(ENCODING);
	private WorkerPool<PntcTileWork> tileCreatorPool;
	private DBLocalManager dbManager;
	private SqliteConfig config;

	public SqliteGenerator(SqliteConfig config, DBLocalManagerFactory dbManagerFactory) {
		this.config = config;
		dbManager = dbManagerFactory.createDBManager();
	}

	public boolean createsqlite(List<ShapePoint> plotList,String radarKey) throws PntcGenerationException {
		try {
			Logger.info("Creating a temporary database in the output folder.");
			dbManager.createConnection(false);
			dbManager.createDataTable();

			Logger.info("Reading point cloud data from source files and import into the database.");
			writeSqliteFile(plotList,radarKey);

			Logger.info("Creating database index. It may take a while depending on the size of input data");
			dbManager.createIndexes();
		} catch (Exception e) {
			throw new PntcGenerationException("Faild to read and import source data into the local temporary database.", e);
		}
		return true;
	}

	private void writeSqliteFile(List<ShapePoint> plotList, String radarKey) throws IOException, SQLException {
		List<ShapePoint> batchPointList = new ArrayList<ShapePoint>();
		for (ShapePoint shapePoint : plotList) {
			batchPointList.add(shapePoint);
			if (batchPointList.size() % SqliteDBManager.batchInsertionSize == 0) {
				dbManager.importIntoDatabase(batchPointList,radarKey);
				batchPointList = new ArrayList<ShapePoint>();
			}
		}
		dbManager.importIntoDatabase(batchPointList,radarKey);
	}

	public List<ShapePoint> readPointList() throws PntcGenerationException {
		try {
			dbManager.createConnection(true);
			return dbManager.readPointList();
		}catch (Exception e) {
			throw new PntcGenerationException("Faild to read point list from database.", e);
		}
	}


}
