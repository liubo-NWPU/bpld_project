package com.gis.manager.utils.cesium;

/**
 * @Author:wangmeng
 * @Date:创建于2018/12/5 18:04
 * @Description:笛卡尔坐标(世界坐标)
 */
public class Cartesian3 {
    private double x;
    private double y;
    private double z;
    public Cartesian3(){
        this(0,0,0);
    }

    public Cartesian3(double x, double y, double z){
        this.x=x;
        this.y=y;
        this.z=z;
    }
    public double getX() {
        return x;
    }
    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }
    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }
    public void setZ(double z) {
        this.z = z;
    }

    public static Cartesian3 subtract(Cartesian3 left, Cartesian3 right) {
        Cartesian3 result = new Cartesian3();
        result.setX(left.getX()-right.getX());
        result.setY(left.getY()-right.getY());
        result.setZ(left.getZ()-right.getZ());
        return result;
    }

    public static double magnitudeSquared(Cartesian3 cartesian){
        return cartesian.getX()*cartesian.getX()+cartesian.getY()*cartesian.getY()+cartesian.getZ()*cartesian.getZ();
    }

    public static double magnitude(Cartesian3 cartesian){
        return Math.sqrt(Cartesian3.magnitudeSquared(cartesian));
    }

    public static double dot(Cartesian3 left, Cartesian3 right){
        return left.getX() * right.getX() + left.getY() * right.getY() + left.getZ() * right.getZ();
    }

    public static Cartesian3 add(Cartesian3 left, Cartesian3 right) {
        return new Cartesian3(left.getX()+right.getX(),left.getY()+right.getY(),left.getZ()+right.getZ());
    }

    public static Cartesian3 multiplyComponents(Cartesian3 left, Cartesian3 right) {
        Cartesian3 result = new Cartesian3();
        result.setX(left.getX()*right.getX());
        result.setY(left.getY()*right.getY());
        result.setZ(left.getZ()*right.getZ());
        return result;
    }

    public static Cartesian3 normalize(Cartesian3 cartesian) {
        double magnitude = Cartesian3.magnitude(cartesian);
        Cartesian3 result = new Cartesian3();
        result.setX(cartesian.getX()/magnitude);
        result.setY(cartesian.getY()/magnitude);
        result.setZ(cartesian.getZ()/magnitude);
        return result;
    }

    public static Cartesian3 clone(Cartesian3 cartesian) {
        return new Cartesian3(cartesian.getX(), cartesian.getY(), cartesian.getZ());
    }

    public static Cartesian3 multiplyByScalar(Cartesian3 cartesian, double ratio) {
        return new Cartesian3(cartesian.getX()*ratio,cartesian.getY()*ratio,cartesian.getZ()*ratio);
    }

    public static double maximumComponent(Cartesian3 cartesian) {
        return Math.max(Math.max(cartesian.getX(), cartesian.getY()),cartesian.getZ());
    }
}
