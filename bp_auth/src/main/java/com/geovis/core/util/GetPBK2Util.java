package com.geovis.core.util;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;

public class GetPBK2Util {

    public static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA1";

    /**
     * 生成密文的长度
     */
    public static final int HASH_BIT_SIZE = 64 * 4;

    /**
     * 迭代次数
     */
    public static final int PBKDF2_ITERATIONS = 2000;
    /**
     * PBKDF2加密
     * @param msg
     * @return
     */
    public static String encryptPBKDF(String msg,String saltInf) {
        try {
            char[] chars = msg.toCharArray();
            byte[]  salt = saltInf.getBytes("UTF-8");
            PBEKeySpec spec = new PBEKeySpec(chars, salt, PBKDF2_ITERATIONS, HASH_BIT_SIZE);
            SecretKeyFactory skf = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
            byte[] hash = skf.generateSecret(spec).getEncoded();
          //  return iterations + toHex(salt) + toHex(hash);
            return toHex(hash);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 二进制字符串转十六进制字符串
     *
     * @param   array       the byte array to convert
     * @return              a length*2 character string encoding the byte array
     */
    public static String toHex(byte[] array) {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if (paddingLength > 0)
            return String.format("%0" + paddingLength + "d", 0) + hex;
        else
            return hex;
    }

}
