package com.gis.trans.utils;

import com.gis.trans.model.ShapePoint;
import com.gis.trans.model.ShapePointVo;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.CRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import java.io.File;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeoToolsUtils {
    /**
     * 将几何对象信息写入一个shapfile文件并读取
     *
     * @throws Exception
     */
    public static void writeSHPbyPoint(String filepath, List<ShapePoint> list) {
        try {
            //创建shape文件对象
            File file = new File(filepath);
            Map<String, Serializable> params = new HashMap<String, Serializable>();
            params.put(ShapefileDataStoreFactory.URLP.key, file.toURI().toURL());
            ShapefileDataStore ds = (ShapefileDataStore) new ShapefileDataStoreFactory().createNewDataStore(params);
            //定义图形信息和属性信息
            SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
              CoordinateReferenceSystem decode = CRS.decode("EPSG:3857");
            //     CoordinateReferenceSystem decode =CRS.parseWKT("PROJCS[\"WGS_1984_Web_Mercator_Auxiliary_Sphere\",GEOGCS[\"GCS_WGS_1984\",DATUM[\"D_WGS_1984\",SPHEROID[\"WGS_1984\",6378137.0,298.257223563]],PRIMEM[\"Greenwich\",0.0],UNIT[\"Degree\",0.0174532925199433]],PROJECTION[\"Mercator_Auxiliary_Sphere\"],PARAMETER[\"False_Easting\",0.0],PARAMETER[\"False_Northing\",0.0],PARAMETER[\"Central_Meridian\",0.0],PARAMETER[\"Standard_Parallel_1\",0.0],PARAMETER[\"Auxiliary_Sphere_Type\",0.0],UNIT[\"Meter\",1.0]]");
            /*CoordinateReferenceSystem decode =CRS.parseWKT("Name: GCS_WGS_1984\n" +
                    "Angular Unit: Degree (0.0174532925199433)\n" +
                    "Prime Meridian: Greenwich (0.0)\n" +
                    "Datum: D_WGS_1984\n" +
                    "  Spheroid: WGS_1984\n" +
                    "    Semimajor Axis: 6378137.0\n" +
                    "    Semiminor Axis: 6356752.314245179\n" +
                    "    Inverse Flattening: 298.257223563");*/
            //  CoordinateReferenceSystem decode = DefaultGeographicCRS.WGS84;
            tb.setCRS(decode);
            tb.setSRS("EPSG:3857");
            tb.setName("shape_file");
            tb.add("the_geom", Point.class);
            tb.add("m", Integer.class);
            tb.add("n", Integer.class);
            tb.add("R_axis", Double.class);
            tb.add("A_axis", Double.class);
            tb.add("strain", Double.class);
            ds.createSchema(tb.buildFeatureType());
            ds.setCharset(Charset.forName("GBK"));
            //设置Writer
            FeatureWriter<SimpleFeatureType, SimpleFeature> writer = ds.getFeatureWriter(ds.getTypeNames()[0], Transaction.AUTO_COMMIT);
            for (int i = 0; i < list.size(); i++) {
                SimpleFeature feature = writer.next();
                addFeatureAttr(feature, list.get(i));
            }
            writer.write();
            writer.close();
            ds.dispose();
        } catch (Exception e) {
        }
    }

    public static void addFeatureAttr(SimpleFeature feature, ShapePoint obj) {
        feature.setAttribute("the_geom", new GeometryFactory().createPoint(new Coordinate(new Double(obj.getX()), new Double(obj.getY()))));
        feature.setAttribute("m", obj.getM());
        feature.setAttribute("n", obj.getN());
        feature.setAttribute("R_axis", obj.getrAxis());
        feature.setAttribute("A_axis", obj.getaAxis());
        feature.setAttribute("strain", obj.getStrain());
    }
}
