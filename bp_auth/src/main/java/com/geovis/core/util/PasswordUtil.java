package com.geovis.core.util;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

import com.geovis.web.domain.system.SysUser;

public class PasswordUtil {

    private static String algorithmName = "md5";

    private static int hashIterations = 2;

    public static String encryptPassword(String password){
        String newPassword = new SimpleHash(algorithmName, password, hashIterations).toHex();

        return newPassword;
    }

    public static String encryptPassword(String password, String salt){
        if(salt == null || "".equals(salt)){
            return PasswordUtil.encryptPassword(password);
        }

        String newPassword = new SimpleHash(algorithmName, password,
                ByteSource.Util.bytes(salt), hashIterations).toHex();

        return newPassword;
    }

    public static void encryptPassword(SysUser user) {
        String newPassword = new SimpleHash(algorithmName, user.getPassword(),
                ByteSource.Util.bytes(user.getUsername() + user.getSalt()), hashIterations).toHex();
        user.setPassword(newPassword);
    }

//    public static void main(String[] args) {
//        SysUser user = new SysUser();
//        user.setUsername("admin");
//        user.setPassword("admin");
//        user.setSalt("yan");
//        PasswordUtil.encryptPassword(user);
//        System.out.println(user.getPassword());
//        String pwd = PasswordUtil.encryptPassword("admin","adminyan");
//        System.out.println(pwd);
//    }
    
       public static void main(String[] args) {
           String newPassword = new SimpleHash(algorithmName, "e10adc3949ba59abbe56e057f20f883e",
                   ByteSource.Util.bytes("byy1" +"e1571a3cb6"), hashIterations).toHex();
           System.out.println(newPassword);
       }


}
