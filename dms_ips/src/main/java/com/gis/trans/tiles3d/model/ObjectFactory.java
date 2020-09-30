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

package com.gis.trans.tiles3d.model;

import com.gis.trans.tiles3d.model.*;
import com.gis.trans.tiles3d.model.Asset;
import com.gis.trans.tiles3d.model.Region;
import com.gis.trans.tiles3d.model.Tile;
import com.gis.trans.tiles3d.model.TileSet;

public class ObjectFactory {
	
	public ObjectFactory() {
    }
	
	public com.gis.trans.tiles3d.model.Tile createTile() {
        return new Tile();
    }

    public com.gis.trans.tiles3d.model.TileSet createTileset() {
        return new TileSet();
    }
    
    public com.gis.trans.tiles3d.model.Asset createAssect() {
    	return new Asset();
    }
    
    public com.gis.trans.tiles3d.model.Region createRegion() {
    	return new Region();
    }

    public PointCloudModel createPointCloudModel () {
    	return new PointCloudModel();
    }
    
    
}
