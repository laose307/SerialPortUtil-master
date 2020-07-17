package com.serialportlibrary.util;

import java.util.Arrays;

/**
 * Created by Tyhj on 2017/3/10.
 */

public class ByteStringUtil {

    /**
     * 16进制的ascll转10进制
     * @param hex  16进制
     * @return 10进制
     */
    public static String hexAscll2Str(String hex) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < hex.length() - 1; i += 2) {
            String h = hex.substring(i, (i + 2));
            int decimal = Integer.parseInt(h, 16);
            sb.append((char) decimal);
        }
        return sb.toString();
    }




    /**
     * 10进制转ASCII
     * @param str 10进制
     * @return ascll
     */
    public static String strToAscll(String str) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            sb.append(Integer.toString(c, 10));
        }
        return sb.toString();
    }




    public static byte[] hexStrToByteArray(String str) {
        if (str == null) {
            return null;
        }
        if (str.length() == 0) {
            return new byte[0];
        }
        byte[] byteArray = new byte[str.length() / 2];
        for (int i = 0; i < byteArray.length; i++){
            String subStr = str.substring(2 * i, 2 * i + 2);
            byteArray[i] = ((byte)Integer.parseInt(subStr, 16));
        }
        return byteArray;
    }

    public static String byteArrayToHexStr(byte[] byteArray) {
        if (byteArray == null){
            return null;
        }
        char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[byteArray.length * 2];
        for (int j = 0; j < byteArray.length; j++) {
            int v = byteArray[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }


    public static String StrToAddHexStr(String[] strings){
        long all=Long.parseLong("0",16);
        for (int i=0;i<strings.length;i++){
            long one=Long.parseLong(strings[i],16);
            all=all+one;
        }
        return Long.toHexString(256-(all%256));
    }


    public static void main(String[] args){
        System.out.println(Arrays.toString(hexStrToByteArray("55AA0100010001")));
    }

}