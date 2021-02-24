package com.serialportlibrary.service.impl;

import android.util.Log;

import com.serialportlibrary.service.SerialPortListening;

import java.io.IOException;

/**
 * 异步
 */
public class SerialPortASyncBuilder {

    /**
     * 读取返回结果超时时间
     */
    private Long mTimeOut = 0L;
    /**
     * 串口地址
     */
    private String mDevicePath;


    /**
     * 抖动过滤
     */
    private boolean shakeFliter;

    /**
     * 回调数据
     */
    private SerialPortListening mSerialPortListening;
    /**
     * 波特率
     */
    private int mBaudrate;


    public SerialPortListening getSerialPortListening() {
        return mSerialPortListening;
    }

    public SerialPortASyncBuilder setSerialPortListening(SerialPortListening mSerialPortListening) {
        this.mSerialPortListening = mSerialPortListening;
        return this;
    }

    public SerialPortASyncBuilder setBaudrate(int baudrate) {
        mBaudrate = baudrate;
        return this;
    }

    public boolean isShakeFliter() {
        return shakeFliter;
    }

    public SerialPortASyncBuilder setShakeFliter(boolean shakeFliter) {
        this.shakeFliter = shakeFliter;
        return this;
    }

    public SerialPortASyncBuilder setDevicePath(String devicePath) {
        mDevicePath = devicePath;
        return this;
    }


    public SerialPortASyncBuilder setTimeOut(Long timeOut) {
        mTimeOut = timeOut;
        return this;
    }

    public SerialPortASynService createService() {
        SerialPortASynService serialPortService = null;
        try {
            Log.d("SerialPortMainActivity",toString());
            serialPortService = new SerialPortASynService(mDevicePath, mBaudrate, mTimeOut,shakeFliter,mSerialPortListening);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return serialPortService;
    }

    @Override
    public String toString() {
        return "SerialPortBuilder{" +
                "mTimeOut=" + mTimeOut +
                ", mDevicePath='" + mDevicePath + '\'' +
                ", shakeFliter=" + shakeFliter +
                ", mBaudrate=" + mBaudrate +
                '}';
    }
}
