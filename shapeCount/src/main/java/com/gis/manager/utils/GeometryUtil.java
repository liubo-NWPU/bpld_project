package com.gis.manager.utils;
import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import org.gdal.osr.CoordinateTransformation;
import org.gdal.osr.SpatialReference;
import org.geotools.referencing.CRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * @Author:wangmeng
 * @Date:创建于2018/1/22 18:53
 * @Description:
 */
public class GeometryUtil {
    private static GeometryFactory geometryFactory = new GeometryFactory();
    public static int DefaultSRID=4326;
    private static SpatialReference targetReference;
    public static int ProjectedSRID = 3857;
    private static SpatialReference projectedReference;

    public static SpatialReference getTargetReference(){
        if(targetReference==null){
            targetReference = new SpatialReference();
            targetReference.ImportFromEPSG(DefaultSRID);
        }
        return targetReference;
    }

    public static SpatialReference getProjectedReference(){
        if(projectedReference==null){
            projectedReference = new SpatialReference();
            projectedReference.ImportFromEPSG(ProjectedSRID);
        }
        return projectedReference;
    }
    /**
     * create a point
     * @return
     */
    public static Point createPoint(double x,double y){
        //Coordinate coord = new Coordinate(109.013388, 32.715519);
        Coordinate coord = new Coordinate(x, y);
        Point point = geometryFactory.createPoint(coord);
        return point;
    }

    /**
     * create a point
     * @return
     */
    public static Point createPoint(Coordinate coord){
        //Coordinate coord = new Coordinate(109.013388, 32.715519);
        Point point = geometryFactory.createPoint(coord);
        return point;
    }

    /**
     * create a point by WKT
     * @return
     * @throws ParseException
     */
    public static Point createPointByWKT(String wkt) throws ParseException {
        WKTReader reader = new WKTReader( geometryFactory );
        //Point point = (Point) reader.read("POINT (109.013388 32.715519)");
        Point point = (Point) reader.read(wkt);
        return point;
    }

    /**
     * create multiPoint by wkt
     * @return
     */
    public static MultiPoint createMulPointByWKT(String wkt)throws ParseException{
        WKTReader reader = new WKTReader( geometryFactory );
        //MultiPoint mpoint = (MultiPoint) reader.read("MULTIPOINT(109.013388 32.715519,119.32488 31.435678)");
        MultiPoint mpoint = (MultiPoint) reader.read(wkt);
        return mpoint;
    }
    /**
     *
     * create a line
     * @return
     */
    public static LineString createLine(){
        Coordinate[] coords  = new Coordinate[] {new Coordinate(2, 2), new Coordinate(2, 2)};
        LineString line = geometryFactory.createLineString(coords);
        return line;
    }

    /**
     * create a line by WKT
     * @return
     * @throws ParseException
     */
    public static LineString createLineByWKT(String wkt) throws ParseException{
        WKTReader reader = new WKTReader( geometryFactory );
        //LineString line = (LineString) reader.read("LINESTRING(0 0, 2 0)");
        LineString line = (LineString) reader.read(wkt);
        return line;
    }

    /**
     * create multiLine
     * @return
     */
    public MultiLineString createMLine(){
        Coordinate[] coords1  = new Coordinate[] {new Coordinate(2, 2), new Coordinate(2, 2)};
        LineString line1 = geometryFactory.createLineString(coords1);
        Coordinate[] coords2  = new Coordinate[] {new Coordinate(2, 2), new Coordinate(2, 2)};
        LineString line2 = geometryFactory.createLineString(coords2);
        LineString[] lineStrings = new LineString[2];
        lineStrings[0]= line1;
        lineStrings[1] = line2;
        MultiLineString ms = geometryFactory.createMultiLineString(lineStrings);
        return ms;
    }

    /**
     * create multiLine by WKT
     * @return
     * @throws ParseException
     */
    public static MultiLineString createMLineByWKT(String wkt)throws ParseException{
        WKTReader reader = new WKTReader( geometryFactory );
        //MultiLineString line = (MultiLineString) reader.read("MULTILINESTRING((0 0, 2 0),(1 1,2 2))");
        MultiLineString line = (MultiLineString) reader.read(wkt);
        return line;
    }

    /**
     * create a polygon(多边形) by WKT
     * @return
     * @throws ParseException
     */
    public static Polygon createPolygonByWKT(String wkt){
        WKTReader reader = new WKTReader(geometryFactory);
        Polygon polygon = null;
        try {
            polygon = (Polygon) reader.read(wkt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return polygon;
    }

    public static Geometry createGeometryByWKT(String wkt){
        WKTReader reader = new WKTReader( geometryFactory );
        Geometry geometry = null;
        try {
            geometry = reader.read(wkt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return geometry;
    }

    public static Polygon createPolygonByExtent(double minLon,double minLat,double maxLon,double maxLat){
        Polygon polygon=createPolygonByCoords(minLon,minLat,maxLon,minLat,maxLon,maxLat,minLon,maxLat);
        return polygon;
    }
    public static Polygon createPolygonByCoords(double x1,double y1,double x2,double y2,double x3,double y3,double x4,double y4){
        WKTReader reader = new WKTReader( geometryFactory );
        String wkt=String.format("POLYGON((%f %f,%f %f,%f %f,%f %f,%f %f))", x1,y1,x2,y2,x3,y3,x4,y4,x1,y1);
        Polygon polygon=null;
        try {
            polygon = (Polygon) reader.read(wkt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return polygon;
    }
    public static Polygon createPolygon(Coordinate[] coords){
        Polygon polygon=geometryFactory.createPolygon(coords);
        return polygon;
    }
    /**
     * create GeometryCollection  contain point or multiPoint or line or multiLine or polygon or multiPolygon
     * @return
     * @throws ParseException
     */
    public GeometryCollection createGeoCollect() throws ParseException{
        LineString line = createLine();
        Polygon poly =  createPolygonByWKT("POLYGON((20 10, 30 0, 40 10, 30 20, 20 10))");
        Geometry g1 = geometryFactory.createGeometry(line);
        Geometry g2 = geometryFactory.createGeometry(poly);
        Geometry[] garray = new Geometry[]{g1,g2};
        GeometryCollection gc = geometryFactory.createGeometryCollection(garray);
        return gc;
    }

    /**
     * create a Circle  创建一个圆，圆心(x,y) 半径RADIUS
     * @param x
     * @param y
     * @param RADIUS
     * @return
     */
    public static Polygon createCircle(double x, double y, final double RADIUS){
        final int SIDES = 32;//圆上面的点个数
        Coordinate coords[] = new Coordinate[SIDES+1];
        for( int i = 0; i < SIDES; i++){
            double angle = ((double) i / (double) SIDES) * Math.PI * 2.0;
            double dx = Math.cos( angle ) * RADIUS;
            double dy = Math.sin( angle ) * RADIUS;
            coords[i] = new Coordinate( (double) x + dx, (double) y + dy );
        }
        coords[SIDES] = coords[0];
        LinearRing ring = geometryFactory.createLinearRing( coords );
        Polygon polygon = geometryFactory.createPolygon( ring, null );
        return polygon;
    }

    public static Coordinate[] project(Coordinate[] coords,SpatialReference sourceReference,SpatialReference targetReferenc){
        CoordinateTransformation ctf = CoordinateTransformation.CreateCoordinateTransformation(sourceReference, targetReferenc);
        Coordinate[] result = new Coordinate[coords.length];
        double[] tempValues;
        for (int i = 0; i < coords.length; i++) {
            tempValues = ctf.TransformPoint(coords[i].x, coords[i].y);
            result[i] = new Coordinate(tempValues[0], tempValues[1]);
        }
        return result;
    }

    public static Integer getEPSG(String wkt){
        Integer epsg = null;
        try{
            CoordinateReferenceSystem crs = CRS.parseWKT(wkt);
            epsg = CRS.lookupEpsgCode(crs, false);
        }
        catch (Exception ex){
            System.out.println("获取epsg错误！");
        }
        return epsg;
    }
}
