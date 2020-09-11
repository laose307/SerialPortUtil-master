package com.serialportlibrary.service.impl;

import android.os.SystemClock;

import android.serialport.SerialPort;
import android.util.Log;

import com.serialportlibrary.service.ISerialPortService;
import com.serialportlibrary.service.SerialPortListening;
import com.serialportlibrary.util.ByteStringUtil;
import com.serialportlibrary.util.LogUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SerialPortService implements ISerialPortService {

    private ExecutorService mExecutorService = Executors.newFixedThreadPool(1);//实例显示池
    /**
     * 上一次输出的数据
     */
    private String preOutData="";

    /**
     * 当前的输出的数据
     */
    private String curOutData;


    /**
     * 尝试读取数据间隔时间
     */
    private static int RE_READ_WAITE_TIME = 12;

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

    /**
     * 抖动过滤
     */
    private boolean shakeFliter;


    private  SerialPort mSerialPort;

    /**
     * 串口返回监听
     */
    private SerialPortListening mSerialPortListening;

    InputStreamRun mInputStreamRun;


    /**
     * 初始化串口
     * @param devicePath
     * @param baudrate
     * @param timeOut
     * @param shakeFliter
     * @param mSerialPortListening
     * @throws IOException
     */
    public SerialPortService(String devicePath, int baudrate, Long timeOut, boolean shakeFliter, SerialPortListening mSerialPortListening) throws IOException {
        this.shakeFliter=shakeFliter;
        mTimeOut = timeOut;
        mDevicePath = devicePath;
        mBaudrate = baudrate;
        this.mSerialPortListening=mSerialPortListening;
        mSerialPort = new SerialPort(new File(mDevicePath), mBaudrate);

        //没有超时时间、串口通信保持线程常开
        if(mTimeOut==0){

            mInputStreamRun=new InputStreamRun(mSerialPort.getInputStream());
            mExecutorService.execute(mInputStreamRun);
        }

    }

    @Override
    public byte[] sendData(byte[] data) {
        synchronized (SerialPortService.this) {
            try {
                InputStream inputStream = mSerialPort.getInputStream();
                OutputStream outputStream = mSerialPort.getOutputStream();
                int available = inputStream.available();
                byte[] returnData;
                if (available > 0) {
                    returnData = new byte[available];
                    inputStream.read(returnData);
                }
                outputStream.write(data);
                outputStream.flush();
                LogUtil.e("发送数据-------" + Arrays.toString(data));

                if(mTimeOut!=0){
                    LogUtil.e("发送数据-------设置超时时间" + Arrays.toString(data));
                    mInputStreamRun=new InputStreamRun(mSerialPort.getInputStream());
                    mExecutorService.execute(mInputStreamRun);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    private class InputStreamRun implements Runnable {

        private  InputStream inputStream;

        private volatile boolean stop=false;
        public InputStreamRun ( InputStream inputStream){
            this.inputStream=inputStream;
            stop=false;
        }

        @Override
        public void run() {
            Long time = System.currentTimeMillis();
            //暂存每次返回数据长度，不变的时候为读取完数据
            int receiveLeanth = 0;
                while (!stop&&mTimeOut==0?true:System.currentTimeMillis() - time < mTimeOut) {
                    if (inputStream == null) return;
                    try {
                        int   available = inputStream.available();
                        byte[] returnData;
                        if (available > 0 && available == receiveLeanth) {
                            returnData = new byte[available];
                            inputStream.read(returnData);
                            curOutData=Arrays.toString(returnData);
                            if(shakeFliter){
                                if(!preOutData.equals(curOutData)){//启动了抖动过滤，上一次跟当前不一样，返回
                                    preOutData=curOutData;
                                    if(mSerialPortListening!=null){
                                        mSerialPortListening.onReceiveData(returnData);
                                    }
                                }
                            }else{
                                if(mSerialPortListening!=null){
                                    mSerialPortListening.onReceiveData(returnData);
                                }
                            }
                        } else {
                            receiveLeanth = available;
                        }
                        SystemClock.sleep(RE_READ_WAITE_TIME);
                    } catch (IOException e) {
                        e.printStackTrace();
                        break;
                    }

                }
        }
    }


    @Override
    public byte[] sendDataHex(String date) {
        try {
            return sendData(ByteStringUtil.hexStrToByteArray(date.replaceAll("\\s*", "")));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public byte[] sendDataAscll(String data) {
        try {
            return sendData( ByteStringUtil.strToAscll(data.replaceAll("\\s*", "")).getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public byte[] sendDataStrGBK(String data) {
        try {
            return sendData(data.replaceAll("\\s*", "").getBytes("GBK"));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public byte[] sendDataStrUTF(String data) {
        try {
            return sendData(data.replaceAll("\\s*", "").getBytes("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public byte[] sendDataDecimal(int data) {
        try {
            return sendData( ByteStringUtil.int2bytes(data));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void close() {

        if (mSerialPort != null) {
            mSerialPort.closePort();
        }
        if(mInputStreamRun!=null){
            mInputStreamRun.stop=true;
            LogUtil.e("接收dddddd--stop-------" + mInputStreamRun.stop);
        }

        if(mExecutorService!=null){
            long time_out=1*60 ;//超时时间，自己根据任务特点设置
            //第一步，调用shutdown等待在执行的任务和提交等待的任务执行，同时不允许提交任务
            mExecutorService.shutdown();
            try {
                if(!mExecutorService.awaitTermination(time_out, TimeUnit.SECONDS)){
                    //如果等待一段时间后还有任务在执行中被中断或者有任务提交了未执行
                    //1.正在执行被中断的任务需要编写任务代码的时候响应中断
                    List<Runnable> waitToExecuteTaskList = mExecutorService.shutdownNow();
                    //2.处理提交了未执行的任务，一般情况不会出现
                    for(Runnable runnable:waitToExecuteTaskList){
                        //todo
                    }
                }
            }catch (InterruptedException e){//如果被中断了
                //1.正在执行被中断的任务需要编写任务代码的时候响应中断
                List<Runnable> waitToExecuteTaskList = mExecutorService.shutdownNow();
                //2.处理提交了未执行的任务，一般情况不会出现
                for(Runnable runnable:waitToExecuteTaskList){
                    //todo
                }
            }
        }


    }


    public void isOutputLog(boolean debug) {
        LogUtil.isDebug = debug;
    }





}
