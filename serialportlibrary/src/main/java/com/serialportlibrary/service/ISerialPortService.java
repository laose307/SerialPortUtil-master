package com.serialportlibrary.service;
public interface ISerialPortService {

    /**
     * 发送byteArray数据
     *
     * @param data
     * @return
     */
    byte[] sendData(byte[] data);



    /**
     * 发送十六进制的字符串数据
     *
     * @param data
     * @return   byte[]
     */
    byte[] sendDataHex(String data);

    /**
     * 发送字符串 ascll
     * @param data
     * @return
     */


    byte[] sendDataAscll(String data);

    /**
     * 发送字符串 gbk
     * @param data
     * @return
     */
    byte[] sendDataStrGBK(String data);

    /**
     * 发送字符串 utf-8
     * @param data
     * @return
     */
    byte[] sendDataStrUTF(String data);

    /**
     * 发送十进制的字符串数据
     * @param data
     * @return
     */
    byte[] sendDataDecimal(int data);

    void close();
}
