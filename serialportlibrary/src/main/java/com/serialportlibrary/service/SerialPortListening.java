package com.serialportlibrary.service;

public interface SerialPortListening {
    public void onReceiveData(byte[]  receiveData);
}
