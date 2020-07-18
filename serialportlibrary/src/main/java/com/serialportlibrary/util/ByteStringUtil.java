package com.serialportlibrary.util;

import java.util.Arrays;
import java.util.List;

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

    public static int bytesToInt2(byte[] src, int offset) {
        int value;
        value = (int) ( ((src[offset] & 0xFF)<<24)
                |((src[offset+1] & 0xFF)<<16)
                |((src[offset+2] & 0xFF)<<8)
                |(src[offset+3]  & 0xFF));
        return value;
    }
    public static byte[] intToByteArray(int value){
        byte[] byteArray = new byte[4];
        byteArray[0] = (byte) (value & 0xFF);
        byteArray[1] = (byte) (value & 0xFF00);
        byteArray[2] = (byte) (value & 0xFF0000);
        byteArray[3] = (byte) (value & 0xFF000000);
        return byteArray;
    }

    public static byte[] intToBytes2(int n){
        byte[] b = new byte[4];
        for(int i = 0;i < 4;i++){
            b[i] = (byte)(n >> (24 - i * 8));
        }
        return b;
    }

    /**
     * 将byte数组转换为int数据
     * @param b 字节数组
     * @return 生成的int数据
     */
    public static int byteToInt2(byte[] b){
        return (((int)b[0]) << 24) + (((int)b[1]) << 16) + (((int)b[2]) << 8) + b[3];
    }

    public static byte[] int2bytes(int n) {

        int temp1 = 0, temp2 = 0;
        byte[] hex = new byte[2];
        if (n < 256) {
            hex[1] = (byte) n;
        } else {
            temp1 = n & 0xff;
            hex[1] = (byte) temp1;
            temp2 = n >> 8;
            hex[0] = (byte) temp2;
        }
        return hex;
    }

    /**
     * 多个字节数字拼接
     * @param list
     * @return
     */
    public static byte[] byteConcat(List<byte[]> list) {

        //先复制长度
        int byteLength = 0;
        for (byte[] mbyte : list) {
            byteLength = byteLength + mbyte.length;
        }
        byte[] btResult = new byte[byteLength];

        int len = 0;
        for (int i = 0; i < list.size(); i++) {

            if (i != 0) {
                len += list.get(i - 1).length;
            }
            System.arraycopy(list.get(i), 0, btResult, len, list.get(i).length);
        }
        return btResult;
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


    /**
     *
     * @param data1
     * @param data2
     * @return data1 与 data2拼接的结果
     */
    public static byte[] addBytes(byte[] data1, byte[] data2) {
        byte[] data3 = new byte[data1.length + data2.length];
        System.arraycopy(data1, 0, data3, 0, data1.length);
        System.arraycopy(data2, 0, data3, data1.length, data2.length);
        return data3;

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