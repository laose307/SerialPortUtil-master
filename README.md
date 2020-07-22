Android串口通信简单封装，可以用于和连接串口的硬件通信或者进行硬件调试

### 集成方法：

```
//Add the dependency
dependencies {
	         implementation 'com.laose:serialport:0.0.4'
	}
```

### 调用方法
读取文件权限
```java
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
```

获取所有串口地址
```
String[] devicesPath = new SerialPortFinder().getDevicesPaths();
```

打开串口，设置读取返回数据超时时间
```java
    serialPortService = new SerialPortBuilder()
                            .setTimeOut(0L)
                            .setShakeFliter(false)
                            .setBaudrate(baud)
                            .setDevicePath(tty)
                            .setSerialPortListening(new SerialPortListening() {
                                @Override
                                public void onReceiveData(byte[] receiveData) {

                                    }
                                }
                            }).createService();
                    serialPortService.isOutputLog(true);
```

发送指令
```java
//发送byte数组数据
serialPortService.sendData(new byte[2]);

//发送16进制的字符串
 serialPortService.sendDataHex("55AA0101010002");


//发送ascll的字符串
 serialPortService.sendDataAscll("");


```

```


打开或者关闭日志,默认关闭
```java
serialPortService.isOutputLog(true);
```
//关闭串口
```java
serialPortService.close();
```