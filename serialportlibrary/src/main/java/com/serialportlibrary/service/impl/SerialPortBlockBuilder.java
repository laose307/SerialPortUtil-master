package com.serialportlibrary.service.impl;

import java.io.IOException;

/**
 * 阻塞式不开线程即调即返回
 */
public class SerialPortBlockBuilder {

    /**
     * 读取返回结果超时时间
     */
    private Long mTimeOut = 100L;
    /**
     * 串口地址
     */
    private String mDevicePath;

    /**
     * 波特率
     */
    private int mBaudrate;

    public SerialPortBlockBuilder setBaudrate(int baudrate) {
        mBaudrate = baudrate;
        return this;
    }


    public SerialPortBlockBuilder setDevicePath(String devicePath) {
        mDevicePath = devicePath;
        return this;
    }


    public SerialPortBlockBuilder setTimeOut(Long timeOut) {
        mTimeOut = timeOut;
        return this;
    }

    public SerialPortBlockService createService() {
        SerialPortBlockService serialPortService = null;
        try {
            serialPortService = new SerialPortBlockService(mDevicePath, mBaudrate, mTimeOut);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return serialPortService;
    }

}
