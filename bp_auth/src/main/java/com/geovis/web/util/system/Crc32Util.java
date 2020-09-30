package com.geovis.web.util.system;

import java.util.zip.CRC32;

public class Crc32Util {

    public static  long createID(String name){
        long result=0l;
        try {
            CRC32 crc32=new CRC32();
            crc32.update(name.getBytes());
            result=crc32.getValue();
        } catch (Exception e) {

        }
        return result;
    }

    public static void main(String[] args) {
       long result1= Crc32Util.createID("重力数据" +DateTimeUtil.getCurrentYMD());
        long result2= Crc32Util.createID("磁力数据" +DateTimeUtil.getCurrentYMD());

        long result3= Crc32Util.createID("重力数据" +DateTimeUtil.getCurrentYMD());
        long result4= Crc32Util.createID("磁力数据" +DateTimeUtil.getCurrentYMD());

        System.out.println("result1="+result3);

        System.out.println("result2="+result4);

        System.out.println("result3="+result3);

        System.out.println("result4="+result4);

        System.out.println("result1=result3 :"+(result1==result3));
        System.out.println("result2=result4 :"+(result2==result4));
    }

}
