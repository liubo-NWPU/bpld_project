package com.gis.manager.utils.cesium;

/**
 * @Author:wangmeng
 * @Date:创建于2018/12/6 14:25
 * @Description:制图坐标
 */
public class Cartographic {
    private double longitude;
    private double latitude;
    private double height;

    public static Cartesian3 oneOverRadii = new Cartesian3(1.0 / 6378137.0, 1.0 / 6378137.0, 1.0 / 6356752.3142451793);
    public static Cartesian3 oneOverRadiiSquared = new Cartesian3(1.0 / (6378137.0 *  6378137.0), 1.0 / (6378137.0 * 6378137.0), 1.0 / (6356752.3142451793 * 6356752.3142451793));
    public static double centerToleranceSquared = CesiumMath.EPSILON1;

    public Cartographic(){
        this(0,0,0);
    }

    public Cartographic(double longitude,double latitude,double height){
        this.longitude = longitude;
        this.latitude = latitude;
        this.height = height;
    }

    public double getLongitude() {
        return longitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getHeight() {
        return height;
    }
    public void setHeight(double height) {
        this.height = height;
    }

    public static Cartographic fromCartesian(Cartesian3 cartesian){
        Cartesian3 p = ScaleToGeodeticSurface.scaleToGeodeticSurface(cartesian,oneOverRadii,oneOverRadiiSquared,centerToleranceSquared);
        Cartesian3 n = Cartesian3.multiplyComponents(p, oneOverRadiiSquared);
        n=Cartesian3.normalize(n);
        Cartesian3 h = Cartesian3.subtract(cartesian,p);
        double longitude=Math.atan2(n.getY(),n.getX());
        double latitude=Math.asin(n.getZ());
        double height= CesiumMath.sign(Cartesian3.dot(h, cartesian)) * Cartesian3.magnitude(h);
        return new Cartographic(longitude,latitude,height);
    }
}
