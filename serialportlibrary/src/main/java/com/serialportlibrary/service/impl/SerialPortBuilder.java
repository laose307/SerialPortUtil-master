package com.serialportlibrary.service.impl;

import android.util.Log;

import com.serialportlibrary.service.SerialPortListening;

import java.io.IOException;

public class SerialPortBuilder {

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

    public SerialPortBuilder setSerialPortListening(SerialPortListening mSerialPortListening) {
        this.mSerialPortListening = mSerialPortListening;
        return this;
    }

    public SerialPortBuilder setBaudrate(int baudrate) {
        mBaudrate = baudrate;
        return this;
    }

    public boolean isShakeFliter() {
        return shakeFliter;
    }

    public SerialPortBuilder setShakeFliter(boolean shakeFliter) {
        this.shakeFliter = shakeFliter;
        return this;
    }

    public SerialPortBuilder setDevicePath(String devicePath) {
        mDevicePath = devicePath;
        return this;
    }


    public SerialPortBuilder setTimeOut(Long timeOut) {
        mTimeOut = timeOut;
        return this;
    }

    public SerialPortService createService() {
        SerialPortService serialPortService = null;
        try {
            Log.d("SerialPortMainActivity",toString());
            serialPortService = new SerialPortService(mDevicePath, mBaudrate, mTimeOut,shakeFliter,mSerialPortListening);
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
