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

package com.gis.trans.sqlites.database.sql;

import com.gis.trans.model.ShapePoint;
import com.gis.trans.sqlites.SqliteConfig;
import com.gis.trans.sqlites.database.DBLocalManager;
import com.gis.trans.tiles3d.concurrent.PntcQueryResult;
import com.gis.trans.tiles3d.database.DBManager;
import com.gis.trans.tiles3d.model.BoundingBox2D;
import com.gis.trans.tiles3d.model.PointCloudModel;
import com.gis.trans.tiles3d.model.PointObject;
import com.gis.trans.tiles3d.model.Region;
import com.gis.trans.tiles3d.util.CoordianteConverter;
import com.gis.trans.tiles3d.util.CoordinateConversionException;
import com.vividsolutions.jts.geom.Coordinate;

import java.awt.*;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SqliteDBLocal implements DBLocalManager {
	private SqliteConfig config;
	private String dbPath;
	private String dbUrl;
	private String DriverName;
	private Connection connection;	
	public final static int batchInsertionSize = 1000;
	final static int fetchSize = 1000;
	final static String tempDbName = "tmp";
	
	public SqliteDBLocal(SqliteConfig config) {
		this.config = config;
		this.dbPath = config.getFilePath() + File.separator + tempDbName;
		initDB(dbPath);
	}
	
	private void initDB(String dbPath) {
		this.dbUrl = "jdbc:sqlite" + ":" + dbPath;
		this.DriverName = "org.sqlite.JDBC";
	}

	public void createConnection(boolean trans) throws SQLException {
		// register the driver
		try {
			Class.forName(DriverName);
			connection = DriverManager.getConnection(dbUrl);
			connection.setAutoCommit(trans);
		} catch (SQLException|ClassNotFoundException e) {
			throw new SQLException("Faild to connect to database.", e);
		} 
	}

	
	public void createDataTable() throws SQLException {
		String CreateTableCommand = "CREATE TABLE POINT_TABLE (Id integer PRIMARY KEY, "
				+ "m integer, "
				+ "n integer, "
				+ "change double, "
				+ "speed double,"
				+ "accspeed double)";
		Statement stmt = null;
		try {			
			stmt = connection.createStatement();		
			stmt.executeUpdate(CreateTableCommand);
			connection.commit();
		} catch (SQLException e) {
			throw new SQLException("Faild to create data table in database.", e);
		} finally {
			if (stmt != null)
				stmt.close();
		}
	}
	
	public void createIndexes() throws SQLException {
		String CreateIndexCommand = "CREATE INDEX POINT_TABLE_IDX ON POINT_TABLE (m, n)";
		Statement stmt = null;
		try {			
			stmt = connection.createStatement();
			stmt.executeUpdate(CreateIndexCommand);
			connection.commit();
		} catch (SQLException e) {
			throw new SQLException("Faild to create database indexes on data table.", e);
		} finally {
			if (stmt != null)
				stmt.close();
		}	
	}
	public List<ShapePoint> readPointList() throws SQLException {
		List<ShapePoint> resultList = new ArrayList<>();
		ResultSet result = null;
		Statement stmt = null;
		String queryCommand = "SELECT * FROM POINT_TABLE ORDER BY m,n";

		try {
			stmt = connection.createStatement();
			stmt.setFetchSize(fetchSize);
			result = stmt.executeQuery(queryCommand);
			while (result.next()) {
				Long m = result.getLong("m");
				Long n = result.getLong("n");
				Double change = result.getDouble("change");
				Double speed = result.getDouble("speed");
				Double accspeed = result.getDouble("accspeed");

				ShapePoint shapePoint = new ShapePoint();
				shapePoint.setM(m);
				shapePoint.setN(n);
				shapePoint.setStrain(change);
				shapePoint.setSpeed(speed);
				shapePoint.setAccspeed(accspeed);
				resultList.add(shapePoint);
			}
		} catch (Exception e) {
			throw new SQLException("Error occured while read values fetched from database.", e);
		} finally {
			if (result != null)
				result.close();
			if (stmt != null)
				stmt.close();
		}
		return resultList;
	}
	
	public void importIntoDatabase(List<ShapePoint> pointList,String radarKey) throws SQLException {
		String sql = "insert into POINT_TABLE (m,n,change,speed,accspeed) values (?, ?, ?, ?, ?)";
		PreparedStatement ps = null;
		try {	
			ps = connection.prepareStatement(sql);
			Iterator<ShapePoint> iter = pointList.iterator();
			while (iter.hasNext()) {
				ShapePoint pointObject = iter.next();
				ps.setLong(1, pointObject.getM());
				ps.setLong(2, pointObject.getN());
				ps.setDouble(3, pointObject.getStrain());
				ps.setDouble(4, pointObject.getSpeed());
				ps.setDouble(5,pointObject.getAccspeed());
//				ps.setDouble(4, 0.0);
//				ps.setDouble(5,0.0);
//				ps.setString(6,radarKey);
//				ps.setDouble(7,pointObject.getaAxis());
//				ps.setDouble(8,pointObject.getrAxis());
				ps.addBatch();
			}
			ps.executeBatch();
			connection.commit();
		} catch (SQLException e) {
			throw new SQLException("Faild to insert data into database.", e);
		} finally {
			if (ps != null)
				ps.close();
		}		
	}
	
	public Connection getConnection() {
		return connection;
	}
	
	public void KillConnection() {
		try {
			if (!connection.isClosed())
				connection.close();
			dropDatabase();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}

	public void dropDatabase() throws SQLException {				
		try{			
			File file = new File(dbPath);	
			file.delete();  		    	
		}catch(Exception e){ 
			throw new SQLException("Error occured while dropping the database.", e);
    	}		
	}

}
