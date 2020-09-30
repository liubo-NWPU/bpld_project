package com.gis.trans;

import org.gdal.osr.CoordinateTransformation;
import org.gdal.osr.SpatialReference;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.DecimalFormat;

public class test {

    public static void main(String[] args) {
        FileInputStream input = null;
        try {
//            input = new FileInputStream("C:\\Users\\86134\\Downloads\\point_r10\\point_r10\\15\\0_0_0.pnts");
            input = new FileInputStream("D:\\ply\\r10_2020_06_07_05_10_00_to_2020_06_07_05_13_00\\point_r10\\root.pnts");

            byte[] buffer1 = new byte[4];
            input.read(buffer1);
            String str = new String(buffer1);
            System.out.println(str);

            byte[] versionByte = new byte[4];
            input.read(versionByte);
            ByteBuffer.wrap(versionByte).getInt();
            System.out.println(bytesToInt(versionByte,0));

            byte[] byteLength = new byte[4];
            input.read(byteLength);
            System.out.println(bytesToInt(byteLength,0));

            byte[] featureTableJSONByteLength = new byte[4];
            input.read(featureTableJSONByteLength);
            System.out.println(bytesToInt(featureTableJSONByteLength,0));

            byte[] featureTableBinaryByteLength = new byte[4];
            input.read(featureTableBinaryByteLength);
            System.out.println(bytesToInt(featureTableBinaryByteLength,0));

            byte[] batchTableJSONByteLength = new byte[4];
            input.read(batchTableJSONByteLength);
            System.out.println(bytesToInt(batchTableJSONByteLength,0));

            byte[] batchTableBinaryByteLength = new byte[4];
            input.read(batchTableBinaryByteLength);
            System.out.println(bytesToInt(batchTableBinaryByteLength,0));

            byte[] featureJson = new byte[240];
            input.read(featureJson);
            System.out.println(new String(featureJson));

            byte[] feature = new byte[231831];
            input.read(feature);
            byte[] color = new byte[5];
            input.read(color);
            byte[] json = new byte[80];
            input.read(json);
            System.out.println(new String(json));
            byte[] intensity = new byte[51518];
            input.read(intensity);

            byte[] buf = new byte[1024];
            int length = 0;
            //循环读取文件内容，输入流中将最多buf.length个字节的数据读入一个buf数组中,返回类型是读取到的字节数。
            //当文件读取到结尾时返回 -1,循环结束。
            while((length = input.read(buf)) != -1){
                System.out.print(new String(buf,0,length));
            }
//            for (int i = 0;i<1409166;i=i+12)
//            {
//                byte[] x = new byte[4];
//                x[0] = feature[i];
//                x[1] = feature[i+1];
//                System.out.println(byte2float(x,0));
//
//                x[2] = feature[i+2];
//                x[3] = feature[i+3];
//                System.out.println(byte2float(x,0));
//
////                byte[] y = new byte[4];
////                y[0] = feature[i+2];
////                y[1] = feature[i+3];
////                byte[] z = new byte[4];
////                z[0] = feature[i+4];
////                z[1] = feature[i+5];
////                System.out.println(byte2float(x,0));
////                byte[] y = new byte[4];
////                y[0] = feature[i+4];
////                y[1] = feature[i+5];
////                y[2] = feature[i+6];
////                y[3] = feature[i+7];
////                System.out.println(byte2float(y,0));
////                byte[] z = new byte[4];
////                z[0] = feature[i+8];
////                z[1] = feature[i+9];
////                z[2] = feature[i+10];
////                z[3] = feature[i+11];
////                System.out.println(byte2float(z,0));
////                System.out.println(ByteBuffer.wrap(y).getFloat());
////                System.out.println(ByteBuffer.wrap(z).getFloat());
//            }
            input.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static float byte2float(byte[] b, int index) {
        int l;
        l = b[index + 0];
        l &= 0xff;
        l |= ((long) b[index + 1] << 8);
        l &= 0xffff;
        l |= ((long) b[index + 2] << 16);
        l &= 0xffffff;
        l |= ((long) b[index + 3] << 24);
        return Float.intBitsToFloat(l);
    }

    public static float byte2float2(byte[] b, int index) {
        int l;
        l = b[index + 3];
        l &= 0xff;
        l |= ((long) b[index + 2] << 8);
        l &= 0xffff;
        l |= ((long) b[index + 1] << 16);
        l &= 0xffffff;
        l |= ((long) b[index + 0] << 24);
        return Float.intBitsToFloat(l);
    }

    public static int bytesToInt(byte[] src, int offset) {
        int value;
        value = (int) ((src[offset] & 0xFF)
                | ((src[offset+1] & 0xFF)<<8)
                | ((src[offset+2] & 0xFF)<<16)
                | ((src[offset+3] & 0xFF)<<24));
        return value;
    }

    public static int bytesToInt2(byte[] src, int offset) {
        int value;
        value = (int) ( ((src[offset] & 0xFF)<<24)
                |((src[offset+1] & 0xFF)<<16)
                |((src[offset+2] & 0xFF)<<8)
                |(src[offset+3] & 0xFF));
        return value;
    }

    public static void get84() {
        //   wgsbj54.SetWellKnownGeogCS("GCS_Beijing_1954");
//        SpatialReference wgsbj54 = new SpatialReference("PROJCS[\"Beijing 1954 / 3-degree Gauss-Kruger CM 120E\", \n" +  "  GEOGCS[\"Beijing 1954\", \n" + "    DATUM[\"Beijing 1954\", \n" + "      SPHEROID[\"Krassowsky 1940\", 6378245.0, 298.3, AUTHORITY[\"EPSG\",\"7024\"]], \n" + "      TOWGS84[15.8, -154.4, -82.3, 0.0, 0.0, 0.0, 0.0], \n" + "      AUTHORITY[\"EPSG\",\"6214\"]], \n" + "    PRIMEM[\"Greenwich\", 0.0, AUTHORITY[\"EPSG\",\"8901\"]], \n" + "    UNIT[\"degree\", 0.017453292519943295], \n" + "    AXIS[\"Geodetic longitude\", EAST], \n" + "    AXIS[\"Geodetic latitude\", NORTH], \n" + "    AUTHORITY[\"EPSG\",\"4214\"]], \n" + "  PROJECTION[\"Transverse_Mercator\"], \n" + "  PARAMETER[\"central_meridian\", 114.0], \n" + "  PARAMETER[\"latitude_of_origin\", 0.0], \n" + "  PARAMETER[\"scale_factor\", 1.0], \n" + "  PARAMETER[\"false_easting\", 500000.0], \n" + "  PARAMETER[\"false_northing\", 0.0], \n" + "  UNIT[\"m\", 1.0], \n" + "  AXIS[\"Easting\", EAST], \n" + "  AXIS[\"Northing\", NORTH], \n" + "  AUTHORITY[\"EPSG\",\"2437\"]]");
        SpatialReference wgsbj54 = new SpatialReference();
        wgsbj54.ImportFromEPSG(4162);
        System.out.println(wgsbj54.ExportToWkt());
        System.out.println(wgsbj54.EPSGTreatsAsLatLong());

    }

    /**
     * 求B点经纬度
     * @param A 已知点的经纬度，
     * @param distance   AB两地的距离  单位km
     * @param angle  AB连线与正北方向的夹角（0~360）
     * @return  B点的经纬度
     */
    public static double[] getMyLatLng(MyLatLng A,double distance,double angle){

        double dx = distance*1000*Math.sin(Math.toRadians(angle));
        double dy = distance*1000*Math.cos(Math.toRadians(angle));

        double lot = (dx/A.Ed+A.m_RadLo)*180./Math.PI;
        double lat = (dy/A.Ec+A.m_RadLa)*180./Math.PI;
        double[] location = new double[2];
        location[0] = lot;
        location[1] = lat;
        return location;
    }

    /**
     * 获取AB连线与正北方向的角度
     * @param A  A点的经纬度
     * @param B  B点的经纬度
     * @return  AB连线与正北方向的角度（0~360）
     */
    public  static double getAngle(MyLatLng A,MyLatLng B){
        double dx=(B.m_RadLo-A.m_RadLo)*A.Ed;
        double dy=(B.m_RadLa-A.m_RadLa)*A.Ec;
        double angle=0.0;
        angle=Math.atan(Math.abs(dx/dy))*180./Math.PI;
        double dLo=B.m_Longitude-A.m_Longitude;
        double dLa=B.m_Latitude-A.m_Latitude;
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
}
