package com.gis.manager.utils.cesium;

/**
 * @Author:wangmeng
 * @Date:创建于2018/12/10 14:29
 * @Description:
 */
public class Matrix3 {
    private double[] value = new double[9];

    public double[] getValue() {
        return value;
    }
    public void setValue(double[] value) {
        this.value = value;
    }

    public Matrix3(){
        this(0,0,0,0,0,0,0,0,0);
    }

    public Matrix3(double c0r0,double c1r0,double c2r0,double c0r1,double c1r1,double c2r1,double c0r2,double c1r2,double c2r2){
        this.value[0] = c0r0;
        this.value[1] = c1r0;
        this.value[2] = c2r0;
        this.value[3] = c0r1;
        this.value[4] = c1r1;
        this.value[5] = c2r1;
        this.value[6] = c0r2;
        this.value[7] = c1r2;
        this.value[8] = c2r2;
    }

    public static Matrix3 multiply(Matrix3 leftM, Matrix3 rightM) {
        double[] left = leftM.getValue();
        double[] right = rightM.getValue();
        double column0Row0 = left[0] * right[0] + left[3] * right[1] + left[6] * right[2];
        double column0Row1 = left[1] * right[0] + left[4] * right[1] + left[7] * right[2];
        double column0Row2 = left[2] * right[0] + left[5] * right[1] + left[8] * right[2];

        double column1Row0 = left[0] * right[3] + left[3] * right[4] + left[6] * right[5];
        double column1Row1 = left[1] * right[3] + left[4] * right[4] + left[7] * right[5];
        double column1Row2 = left[2] * right[3] + left[5] * right[4] + left[8] * right[5];

        double column2Row0 = left[0] * right[6] + left[3] * right[7] + left[6] * right[8];
        double column2Row1 = left[1] * right[6] + left[4] * right[7] + left[7] * right[8];
        double column2Row2 = left[2] * right[6] + left[5] * right[7] + left[8] * right[8];
        return new Matrix3(column0Row0,column0Row1,column0Row2,
                column1Row0,column1Row1,column1Row2,
                column2Row0,column2Row1,column2Row2
                );
    }

    public static Cartesian3 getColumn(Matrix3 matrix, int index) {
        int startIndex = index * 3;
        double[] mValue = matrix.getValue();
        double x = mValue[startIndex];
        double y = mValue[startIndex + 1];
        double z = mValue[startIndex + 2];
        return new Cartesian3(x,y,z);
    }
}
