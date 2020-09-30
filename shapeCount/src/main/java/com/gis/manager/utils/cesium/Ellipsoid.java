package com.gis.manager.utils.cesium;

/**
 * @Author:wangmeng
 * @Date:创建于2018/12/6 14:21
 * @Description:
 */
public class Ellipsoid {
    private Cartesian3 radii;
    private Cartesian3 radiiSquared;
    private Cartesian3 radiiToTheFourth;
    private Cartesian3 oneOverRadii;
    private Cartesian3 oneOverRadiiSquared;
    private double minimumRadius;
    private double maximumRadius;
    private double centerToleranceSquared;
    private double squaredXOverSquaredZ;

    public static Ellipsoid WGS84 = new Ellipsoid(6378137.0, 6378137.0, 6356752.3142451793);

    private Ellipsoid(double x,double y,double z){
        radii = new Cartesian3(x, y, z);
        radiiSquared = new Cartesian3(x * x,y * y,z * z);
        radiiToTheFourth = new Cartesian3(x * x * x * x,y * y * y * y,z * z * z * z);
        oneOverRadii = new Cartesian3(x == 0.0 ? 0.0 : 1.0 / x,y == 0.0 ? 0.0 : 1.0 / y,z == 0.0 ? 0.0 : 1.0 / z);
        oneOverRadiiSquared = new Cartesian3(x == 0.0 ? 0.0 : 1.0 / (x * x),y == 0.0 ? 0.0 : 1.0 / (y * y), z == 0.0 ? 0.0 : 1.0 / (z * z));
        minimumRadius = Math.min(Math.min(x, y), z);
        maximumRadius = Math.max(Math.max(x, y), z);
        centerToleranceSquared = CesiumMath.EPSILON1;
        if (radiiSquared.getZ()!= 0) {
            squaredXOverSquaredZ = radiiSquared.getX() / radiiSquared.getZ();
        }
    }

    public Cartographic cartesianToCartographic(Cartesian3 cartesian){
        Cartesian3 p = scaleToGeodeticSurface(cartesian);
        Cartesian3 n = geodeticSurfaceNormal(p);
        Cartesian3 h = Cartesian3.subtract(cartesian,p);
        double longitude=Math.atan2(n.getY(),n.getX());
        double latitude=Math.asin(n.getZ());
        double height= CesiumMath.sign(Cartesian3.dot(h, cartesian)) * Cartesian3.magnitude(h);
        return new Cartographic(longitude,latitude,height);
    }

    private Cartesian3 geodeticSurfaceNormal(Cartesian3 cartesian) {
        Cartesian3 result = Cartesian3.multiplyComponents(cartesian,oneOverRadiiSquared);
        return Cartesian3.normalize(result);
    }

    public Cartesian3 scaleToGeodeticSurface(Cartesian3 cartesian){
        return ScaleToGeodeticSurface.scaleToGeodeticSurface(cartesian,oneOverRadii,oneOverRadiiSquared,centerToleranceSquared);
    }
}
