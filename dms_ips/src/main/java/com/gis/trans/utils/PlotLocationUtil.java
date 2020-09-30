package com.gis.trans.utils;

import com.gis.trans.model.ShapePoint;
import com.mathworks.toolbox.javabuilder.MWArray;
import com.mathworks.toolbox.javabuilder.MWCellArray;
import com.mathworks.toolbox.javabuilder.MWException;
import org.springframework.data.geo.Point;
import plotCon.PlotCon;

import java.util.ArrayList;
import java.util.List;

public class PlotLocationUtil {

    /**
     *  add code start by liuyan 20200805
     */

    /**
     * 自定义坐标类
     */
    static class MyLatLng {
        final static double Rc=6378137;
        final static double Rj=6356725;
        double m_LoDeg,m_LoMin,m_LoSec;
        double m_LaDeg,m_LaMin,m_LaSec;
        double m_Longitude,m_Latitude;
        double m_RadLo,m_RadLa;
        double Ec;
        double Ed;
        public MyLatLng(double longitude,double latitude){
            m_LoDeg=(int)longitude;
            m_LoMin=(int)((longitude-m_LoDeg)*60);
            m_LoSec=(longitude-m_LoDeg-m_LoMin/60.)*3600;

            m_LaDeg=(int)latitude;
            m_LaMin=(int)((latitude-m_LaDeg)*60);
            m_LaSec=(latitude-m_LaDeg-m_LaMin/60.)*3600;

            m_Longitude=longitude;
            m_Latitude=latitude;
            m_RadLo=longitude*Math.PI/180.;
            m_RadLa=latitude*Math.PI/180.;
            Ec=Rj+(Rc-Rj)*(90.-m_Latitude)/90.;
            Ed=Ec*Math.cos(m_RadLa);
        }
    }

    /**
     * 获取AB连线与正北方向的角度
     * @param pointA  A点的经纬度
     * @param pointB  B点的经纬度
     * @return  AB连线与正北方向的角度（0~360）
     */
    public static double getLineAngleWithNorth(MyLatLng pointA, MyLatLng pointB){
        double dx=(pointB.m_RadLo-pointA.m_RadLo)*pointA.Ed;
        double dy=(pointB.m_RadLa-pointA.m_RadLa)*pointA.Ec;
        double angle=0.0;
        angle=Math.atan(Math.abs(dx/dy))*180./Math.PI;
        double dLo=pointB.m_Longitude-pointA.m_Longitude;
        double dLa=pointB.m_Latitude-pointA.m_Latitude;
        if(dLo>0&&dLa<=0){
            angle=(90.-angle)+90;
        }
        else if(dLo<=0&&dLa<0){
            angle=angle+180.;
        }else if(dLo<0&&dLa>=0){
            angle= (90.-angle)+270;
        }
        return angle;
    }

    /**
     * 求B点经纬度
     * @param pointA 已知点的经纬度，
     * @param distance   AB两地的距离 单位m
     * @param angle  AB连线与正北方向的夹角（0~360）
     * @return  B点的经纬度
     */
    public static Point getLocationWithPointDA(MyLatLng pointA, double distance, double angle){
        double dx = distance * Math.sin(Math.toRadians(angle));
        double dy = distance * Math.cos(Math.toRadians(angle));

        double lot = (dx/pointA.Ed+pointA.m_RadLo)*180./Math.PI;
        double lat = (dy/pointA.Ec+pointA.m_RadLa)*180./Math.PI;
        return new Point(lot, lat);
    }

    /**
     * 求雷达扫描点的坐标
     * @param leftLoc 雷达左轨经纬度，
     * @param rightLoc 雷达右轨经纬度，
     * @param distance   扫描点到雷达的距离 单位M
     * @param radian  扫描点与雷达中垂线的夹角弧度
     * @return  扫描点的经纬度
     */
    public static Point getPlotLocationPlus(Point leftLoc, Point rightLoc, double distance, double radian) {
        //1、计算雷达坐标，通过左右轨的中心点得到
        double leftx = leftLoc.getX();
        double lefty = leftLoc.getY();
        double rightx = rightLoc.getX();
        double righty = rightLoc.getY();
        double centerx = (rightx + leftx) / 2;
        double centery = (righty + lefty) / 2;

        //2、计算雷达左右轨连线与正北方向的夹角
        MyLatLng left = new MyLatLng(leftx,lefty);
        MyLatLng right = new MyLatLng(rightx,righty);
        double radarAngleNorth = getLineAngleWithNorth(left, right);

        //3、计算雷达点和扫描点连线 与 正北方向的夹角
        double pointAngle = 180 * radian / Math.PI;
        double pointAngleNorth = 0.00;
        if (radarAngleNorth >= 0 && radarAngleNorth < 90) {
            pointAngleNorth = 270 + radarAngleNorth + pointAngle;
        }
        else {
            pointAngleNorth = radarAngleNorth - 90 + pointAngle;
        }

        if (pointAngleNorth < 0){
            pointAngleNorth = pointAngleNorth + 360;
        }
        else if (pointAngleNorth >= 360) {
            pointAngleNorth = pointAngleNorth - 360;
        }

        //4、计算扫描点坐标。通过雷达坐标（1），雷达扫描点与正北夹角（3），扫描点到雷达距离，三个变量计算
        MyLatLng radar = new MyLatLng(centerx,centery);
        Point point = getLocationWithPointDA(radar, distance, pointAngleNorth);
        return point;
    }

    /**
     *  add code end by liuyan 20200805
     */

    public static Point getPlotLocation(Point lp, Point rp, double M, double N) {
        double plotAngle = 0.0;
        double x = 0.0;
        double y = 0.0;
        double leftx = lp.getX();
        double lefty = lp.getY();
        double rightx = rp.getX();
        double righty = rp.getY();
        double centerx = (rightx + leftx) / 2;
        double centery = (righty + lefty) / 2;
        double xc = (rightx - leftx);
        double yc = (righty - lefty);
        double ewAngle = Math.atan(yc / xc);  //东西水平倾斜角
       /* if (ewAngle<0){  //判断坐标点偏东还是偏西
            ewAngle = (-ewAngle);
        }else {
            ewAngle +=Math.PI*0.5;
        }
        plotAngle = ewAngle + 0.5 * Math.PI + N; //点倾斜角

        if (plotAngle<Math.PI*0.5){  //由南向北  偏西
            y = M * Math.sin(plotAngle);
            x = -Math.abs(M * Math.cos(plotAngle));
        }else {
            y = M * Math.sin(Math.PI - plotAngle);
            x = Math.abs(M * Math.cos(Math.PI - plotAngle));
        }*/
        plotAngle = ewAngle + Math.PI * 0.5 - N;
        x = M * Math.cos(plotAngle);
        y = M * Math.sin(plotAngle);
        if (leftx>rightx){
            x = -x;
            y = -y;
        }
        return new Point(x + centerx, y + centery);
    }

    public static Point get3857Point(double lon, double lat) {
        lon = lon * 20037508.34 / 180;
        lat = Math.log(Math.tan((90 + lat) * Math.PI / 360)) / (Math.PI / 180);
        lat = lat * 20037508.34 / 180;
        return new Point(lon, lat);
    }

    public static Point getLonLatPoint(double x, double y) {
        Double lon = x / 20037508.34 * 180;
        Double lat = y / 20037508.34 * 180;
        lat = 180 / Math.PI * (2 * Math.atan(Math.exp(lat * Math.PI / 180)) - Math.PI / 2);
        return new Point(lon, lat);
    }


    //获取点的集合 flag all表示所有点，strain表示有效值点
    public static List<ShapePoint> getPlotList(Point left, Point right, String s, String flag) {
        List<ShapePoint> listAll = new ArrayList<ShapePoint>();
        try {
            PlotCon plotCon = new PlotCon();
            Object[] objects = plotCon.plotCon(1, s);
            MWCellArray object = (MWCellArray) objects[0];
            List<MWArray> mwArrays = object.asList();
            int M = Integer.valueOf(mwArrays.get(0).toString()); //像素行数
            int N = Integer.valueOf(mwArrays.get(1).toString()); //像素列数
            MWArray rangeArray = mwArrays.get(2); //雷达与点的距离
            MWArray angleArray = mwArrays.get(3); //弧度
            MWArray strainArray = mwArrays.get(4); //形变量
            int[] rangeints = rangeArray.columnIndex();
            int[] angleints = angleArray.columnIndex();
            double r_axis = 0.0;
            double a_axis = 0.0;
            double strain = 0.0;
            int k = 1;
            for (int i = 1; i < rangeints.length + 1; i++) {
                for (int j = 1; j < angleints.length + 1; j++) {
                    ShapePoint plotInf = new ShapePoint();
                    r_axis = Double.valueOf(rangeArray.get(i).toString());
                    a_axis = Double.valueOf(angleArray.get(j).toString());
                    strain = Double.valueOf(strainArray.get(i + (j - 1) * M).toString());
                    // add by liuyan start 20200805
                    if (strain == -1000 && !flag.equals("all"))
                        continue;
                    //Point plotLocation = PlotLocationUtil.getPlotLocation(left, right, r_axis, a_axis);
                    Point plotLocation = PlotLocationUtil.getPlotLocationPlus(left, right, r_axis, a_axis);
                    // add by liuyan end 20200805
                    plotInf.setM(Long.valueOf(String.valueOf(i)));
                    plotInf.setN(Long.valueOf(String.valueOf(j)));
                    plotInf.setrAxis(r_axis);
                    plotInf.setaAxis(a_axis);
                    plotInf.setX(plotLocation.getX());
                    plotInf.setY(plotLocation.getY());
                    plotInf.setStrain(strain);
                    listAll.add(plotInf);
                }
            }
            return listAll;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
