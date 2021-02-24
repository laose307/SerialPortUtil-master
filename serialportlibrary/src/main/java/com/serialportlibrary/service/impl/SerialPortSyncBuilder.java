package com.serialportlibrary.service.impl;

import java.io.IOException;

/**
 * 同步的，即调用即返回
 */
public class SerialPortSyncBuilder {

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

    public SerialPortSyncBuilder setBaudrate(int baudrate) {
        mBaudrate = baudrate;
        return this;
    }


    public SerialPortSyncBuilder setDevicePath(String devicePath) {
        mDevicePath = devicePath;
        return this;
    }


    public SerialPortSyncBuilder setTimeOut(Long timeOut) {
        mTimeOut = timeOut;
        return this;
    }

    public SerialPortSyncService createService() {
        SerialPortSyncService serialPortService = null;
        try {
            serialPortService = new SerialPortSyncService(mDevicePath, mBaudrate, mTimeOut);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return serialPortService;
    }

}
