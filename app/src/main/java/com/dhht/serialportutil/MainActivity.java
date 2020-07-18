package com.dhht.serialportutil;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.serialport.SerialPortFinder;
import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.serialportlibrary.service.impl.SerialPortBuilder;
import com.serialportlibrary.service.impl.SerialPortService;
import com.serialportlibrary.util.ByteStringUtil;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{




    private Button mBtnOpen,mBtnClose,mBtnSend;
    private EditText mEtSend,tv_select_baud;
    private TextView tv_log,tv_select_path;
    private StringBuffer stringBuffer;
    private TextView tv_result;
    /**
     * 串口名称
     */
    private String PATH = "/dev/ttyS1";
    /**
     * 波特率
     */
    private int BAUDRATE = 115200;
    private SerialPortFinder mSerialPortFinder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mBtnOpen = (Button) findViewById(R.id.btn_open);
        mBtnClose = (Button) findViewById(R.id.btn_close);
        mBtnSend = (Button) findViewById(R.id.btn_send);
        mEtSend = (EditText) findViewById(R.id.et_send);
        tv_log = (TextView) findViewById(R.id.tv_log);
        tv_result = (TextView) findViewById(R.id.tv_result);



        tv_select_path = (TextView) findViewById(R.id.tv_select_path);
        tv_select_baud = (EditText) findViewById(R.id.tv_select_baud);
        stringBuffer = new StringBuffer();
        mBtnOpen.setOnClickListener(this);
        mBtnClose.setOnClickListener(this);
        mBtnSend.setOnClickListener(this);
        tv_select_path.setOnClickListener(this);
        tv_select_baud.setOnClickListener(this);
        mSerialPortFinder = new SerialPortFinder();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_open:

                get_root(MainActivity.this);
                if(TextUtils.isEmpty(tv_select_baud.getText().toString())){

                    Toast.makeText(MainActivity.this,"请输入波特率", Toast.LENGTH_LONG).show();
                    return;
                }

//                BAUDRATE= Integer.valueOf(tv_select_baud.getText().toString());
//
//
//                openPort();
                break;
            case R.id.btn_close:
                closePort();
                break;
            case R.id.btn_send:
                stringBuffer = new StringBuffer();
                String cmd = mEtSend.getText().toString();
                if(TextUtils.isEmpty(cmd)){
                    Toast.makeText(this,"请输入指令！",Toast.LENGTH_SHORT).show();

                    return;
                }
                /*sendParams(bytes);*/
                String text = mEtSend.getText().toString();
                setSerialPort(text);
                break;
            case R.id.tv_select_path:
                List<String> list = getAllDevicesPath();
                if (list == null || list.size() <= 0) {
                    Toast.makeText(this, "木有串口设备哦", Toast.LENGTH_SHORT).show();
                    return;
                }
                OneColumDialog dialog = new OneColumDialog(this, list, new OneColumDialog.SelectListener() {
                    @Override
                    public void selected(int position, String value) {
                        tv_select_path.setText(value);
                    }
                });
                dialog.show();
                break;
            case R.id.tv_select_baud:

                break;
            default:

                break;
        }
    }

    static public byte HexToByte(String inHex)//Hex字符串转byte
    {
        return (byte) Integer.parseInt(inHex,16);
    }
    /**
     * 获取全部窗口地址
     *
     * @return
     */
    public List<String> getAllDevicesPath() {
        return Arrays.asList(mSerialPortFinder.getDevicesPaths());
    }

    SerialPortService serialPortService;
    public void setSerialPort(String cmd){

         serialPortService = new SerialPortBuilder()
                .setTimeOut(1000000000L)
                .setBaudrate(Integer.valueOf(tv_select_baud.getText().toString()))
                .setDevicePath(tv_select_path.getText().toString())
                .createService();
        serialPortService.isOutputLog(true);
//发送开门指令
        byte[] receiveData = serialPortService.sendDataHex(cmd);

        if(receiveData!=null){
            Log.e("MainActivity：", ByteStringUtil.byteArrayToHexStr(receiveData));


            setResult(ByteStringUtil.byteArrayToHexStr(receiveData));



        }else{
            Log.e("MainActivity：", "收不到数据");
        }
    }


    @UiThread
    public void setResult(String result){
        tv_result.setText(result);
    }

    /**
     * 打开串口
     */
    private void openPort(){
        String path = tv_select_path.getText().toString();
        if("".equals(path)){
            Toast.makeText(this,"请选择串口号", Toast.LENGTH_SHORT).show();
            return;
        }

        setSerialPort("");
    }

    /**
     * 关闭串口
     */
    private void closePort(){
        serialPortService.close();
    }


    //转hex字符串转字节数组
    static public byte[] HexToByteArr(String inHex)//hex字符串转字节数组
    {
        int hexlen = inHex.length();
        byte[] result;
        if (isOdd(hexlen)==1)
        {//奇数
            hexlen++;
            result = new byte[(hexlen/2)];
            inHex="0"+inHex;
        }else {//偶数
            result = new byte[(hexlen/2)];
        }
        int j=0;
        for (int i = 0; i < hexlen; i+=2)
        {
            result[j]=HexToByte(inHex.substring(i,i+2));
            j++;
        }
        return result;
    }
    // 判断奇数或偶数，位运算，最后一位是1则为奇数，为0是偶数
    static public int isOdd(int num)
    {
        return num & 0x1;
    }
    // 获取ROOT权限
    public void get_root(Context mCtx){

        if (is_root()){
            Toast.makeText(mCtx, "已经具有ROOT权限!", Toast.LENGTH_LONG).show();
        }
        else{
            try{
                ProgressDialog progress_dialog = ProgressDialog.show(mCtx,
                        "ROOT", "正在获取ROOT权限...", true, false);
                Runtime.getRuntime().exec("su");
            }
            catch (Exception e){
                Toast.makeText(mCtx, "获取ROOT权限时出错!", Toast.LENGTH_LONG).show();
            }
        }

    }
    // 判断是否具有ROOT权限
    public static boolean is_root() {

        boolean res = false;

        try {
            if ((!new File("/system/bin/su").exists()) &&
                    (!new File("/system/xbin/su").exists())) {
                res = false;
            } else {
                res = true;
            }
            ;
        } catch (Exception e) {

        }
        return res;
    }

    public void zhiling(View view) {

        setSerialPort(":T!");
    }

    public void qupi(View view) {
        setSerialPort(":Z!");
    }
}
