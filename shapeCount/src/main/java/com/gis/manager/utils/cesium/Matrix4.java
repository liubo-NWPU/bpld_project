package com.gis.manager.utils.cesium;

import java.util.List;

/**
 * @Author:wangmeng
 * @Date:创建于2018/12/10 11:19
 * @Description:
 */
public class Matrix4 {
    private double[] value = new double[16];

    public double[] getValue() {
        return value;
    }
    public void setValue(double[] value) {
        this.value = value;
    }

    public Matrix4(){
        this(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0);
    }

    public Matrix4(double c0r0,double c1r0,double c2r0,double c3r0,double c0r1,double c1r1,double c2r1,double c3r1,double c0r2,double c1r2,double c2r2,double c3r2,double c0r3,double c1r3,double c2r3,double c3r3){
        this.value[0] = c0r0;
        this.value[1] = c1r0;
        this.value[2] = c2r0;
        this.value[3] = c3r0;
        this.value[4] = c0r1;
        this.value[5] = c1r1;
        this.value[6] = c2r1;
        this.value[7] = c3r1;
        this.value[8] = c0r2;
        this.value[9] = c1r2;
        this.value[10] = c2r2;
        this.value[11] = c3r2;
        this.value[12] = c0r3;
        this.value[13] = c1r3;
        this.value[14] = c2r3;
        this.value[15] = c3r3;
    }

    public static Matrix4 IDENTITY = new Matrix4(1.0, 0.0, 0.0, 0.0,
            0.0, 1.0, 0.0, 0.0,
            0.0, 0.0, 1.0, 0.0,
            0.0, 0.0, 0.0, 1.0);

    public static Matrix4 unpack(List<Double> transform) {
        return new Matrix4(transform.get(0).doubleValue(),transform.get(1).doubleValue(),transform.get(2).doubleValue(),transform.get(3).doubleValue(),
                transform.get(4).doubleValue(),transform.get(5).doubleValue(),transform.get(6).doubleValue(),transform.get(7).doubleValue(),
                transform.get(8).doubleValue(),transform.get(9).doubleValue(),transform.get(10).doubleValue(),transform.get(11).doubleValue(),
                transform.get(12).doubleValue(),transform.get(13).doubleValue(),transform.get(14).doubleValue(),transform.get(15).doubleValue());
    }

    public static Cartesian3 multiplyByPoint(Matrix4 matrix, Cartesian3 cartesian) {
        double vX = cartesian.getX();
        double vY = cartesian.getY();
        double vZ = cartesian.getZ();

        double maxtrixValue[] =  matrix.getValue();

        double x = maxtrixValue[0] * vX + maxtrixValue[4] * vY + maxtrixValue[8] * vZ + maxtrixValue[12];
        double y = maxtrixValue[1] * vX + maxtrixValue[5] * vY + maxtrixValue[9] * vZ + maxtrixValue[13];
        double z = maxtrixValue[2] * vX + maxtrixValue[6] * vY + maxtrixValue[10] * vZ + maxtrixValue[14];
        return new Cartesian3(x,y,z);
    }

    public static Cartesian3 getScale(Matrix4 transform){
        Cartesian3 cartesian3 = new Cartesian3();

        double maxtrixValue[] =  transform.getValue();

        cartesian3.setX(Cartesian3.magnitude(new Cartesian3(maxtrixValue[0],maxtrixValue[1],maxtrixValue[2])));
        cartesian3.setY(Cartesian3.magnitude(new Cartesian3(maxtrixValue[4],maxtrixValue[5],maxtrixValue[6])));
        cartesian3.setZ(Cartesian3.magnitude(new Cartesian3(maxtrixValue[8],maxtrixValue[9],maxtrixValue[10])));
        return cartesian3;
    }

    public static Matrix3 getRotation(Matrix4 transform) {
        double[] tfValue = transform.getValue();
        return new Matrix3(tfValue[0],tfValue[1],tfValue[2],
                tfValue[4],tfValue[5],tfValue[6],
                tfValue[8],tfValue[9],tfValue[10]
                );
    }
}
