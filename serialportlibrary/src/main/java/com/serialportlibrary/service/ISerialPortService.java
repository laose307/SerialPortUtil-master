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
    byte[] sendData(String data);

    void close();
}
