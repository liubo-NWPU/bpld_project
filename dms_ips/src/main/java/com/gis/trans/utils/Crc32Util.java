package com.gis.trans.utils;

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
}
