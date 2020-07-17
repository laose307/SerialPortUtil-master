package com.dhht.serialportutil;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.serialport.SerialPortFinder;
import android.support.annotation.MainThread;
import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.dhht.serialportutil.db.DBTableHelper;
import com.dhht.serialportutil.db.SerialPortCmdData;
import com.dhht.serialportutil.utils.ACache;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.serialportlibrary.service.SerialPortListening;
import com.serialportlibrary.service.impl.SerialPortBuilder;
import com.serialportlibrary.service.impl.SerialPortService;
import com.serialportlibrary.util.ByteStringUtil;

import java.io.File;
import java.util.Arrays;
import java.util.List;


public class SerialPortMainActivity extends AppCompatActivity  {


    public static String TAG="SerialPortMainActivity";


    private SerialPortFinder mSerialPortFinder;

    //超时
    private String mTimeOut="";

    Spinner spinner_tty,spinner_baud,spinner_timeout;
    ToggleButton toggle_open_close,spinner_shakefilter;
    RadioButton radio_sendhex,radio_sendascll,radio_sendtext;
    RadioButton radio_receivehex,radio_receiveascll,radio_receivetext;
    ListView list_collect;
    EditText edit_cmd;
    TextView serial_result;
    ScrollView scroll_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.serialport_activity_main);
        spinner_tty=(Spinner) findViewById(R.id.spinner_tty);
        spinner_baud=(Spinner) findViewById(R.id.spinner_baud);
        spinner_timeout=(Spinner) findViewById(R.id.spinner_timeout);
        toggle_open_close=(ToggleButton) findViewById(R.id.toggle_open_close);
        spinner_shakefilter=(ToggleButton) findViewById(R.id.spinner_shakefilter);
        serial_result=(TextView) findViewById(R.id.serial_result);
        scroll_view=(ScrollView) findViewById(R.id.scroll_view);
        radio_sendhex=(RadioButton) findViewById(R.id.radio_sendhex);
        radio_sendascll=(RadioButton) findViewById(R.id.radio_sendascll);
        radio_sendtext=(RadioButton) findViewById(R.id.radio_sendtext);

        radio_receivehex=(RadioButton) findViewById(R.id.radio_receivehex);
        radio_receiveascll=(RadioButton) findViewById(R.id.radio_receiveascll);
        radio_receivetext=(RadioButton) findViewById(R.id.radio_receivetext);
        list_collect=(ListView) findViewById(R.id.list_collect);

        edit_cmd=(EditText) findViewById(R.id.edit_cmd);

        mSerialPortFinder=new SerialPortFinder();

        FlowManager.init(this);//这句也可以初始化


        //测试先屏蔽
        spinnerTty();

        //打开关闭串口
        openClose();

        listView();


        //选择配置

        if( ACache.get(SerialPortMainActivity.this).getAsString("buadeindex")!=null){
            int  buadeIndex=Integer.valueOf( ACache.get(SerialPortMainActivity.this).getAsString("buadeindex"));
            spinner_baud.setSelection(buadeIndex);
        }


        if(ACache.get(SerialPortMainActivity.this).getAsString("timeout")!=null){
            int  timeoutIndex= Integer.valueOf(ACache.get(SerialPortMainActivity.this).getAsString("timeout"));
            spinner_timeout.setSelection(timeoutIndex);
        }


        if(ACache.get(SerialPortMainActivity.this).getAsObject("shakefilter")!=null){
            boolean  shakefilter= (boolean) ACache.get(SerialPortMainActivity.this).getAsObject("shakefilter");

            spinner_shakefilter.setChecked(shakefilter);
        }




    }
     String[] mItems;
    //选择路径
    public void spinnerTty() {

        // 建立数据源
         mItems =   mSerialPortFinder.getDevicesPaths();

        if(mItems==null){
            return;
        }

        // 建立Adapter并且绑定数据源

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, mItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//绑定 Adapter到控件
        spinner_tty .setAdapter(adapter);
        spinner_tty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                mItems =   mSerialPortFinder.getDevicesPaths();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });


    }

    /**
     * 打开串口
     * @param view
     */
    SerialPortService serialPortService;
    public void openClose() {

        toggle_open_close.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){

                   int  baud=Integer.valueOf((getResources().getStringArray(R.array.baudrate)[spinner_baud.getSelectedItemPosition()]));
                   String tty=mItems[spinner_tty.getSelectedItemPosition()];

                    serialPortService = new SerialPortBuilder()
                            .setTimeOut(getTimeOut())
                            .setShakeFliter(spinner_shakefilter.isChecked())
                            .setBaudrate(baud)
                            .setDevicePath(tty)
                            .setSerialPortListening(new SerialPortListening() {
                                @Override
                                public void onReceiveData(byte[] receiveData) {

                                    if(receiveData!=null){
                                        Log.e("MainActivity：", ByteStringUtil.byteArrayToHexStr(receiveData));
                                        String result="";
                                        //16进制
                                        if(radio_receivehex.isChecked()){
                                            result=ByteStringUtil.byteArrayToHexStr(receiveData);
                                        }
                                        //ascll16进制
                                        else   if(radio_receiveascll.isChecked()){
                                            result=ByteStringUtil.hexAscll2Str(ByteStringUtil.byteArrayToHexStr(receiveData));
                                        }
                                        else   if(radio_receivetext.isChecked()){
                                            result=ByteStringUtil.byteArrayToHexStr(receiveData);
                                        }

                                        setResult(result);


                                    }else{
                                        Log.e("MainActivity：", "收不到数据");
                                    }
                                }
                            }).createService();
                    serialPortService.isOutputLog(true);


                    ACache.get(SerialPortMainActivity.this).put("buadeindex",""+spinner_baud.getSelectedItemPosition());
                    ACache.get(SerialPortMainActivity.this).put("timeout",""+spinner_timeout.getSelectedItemPosition());
                    ACache.get(SerialPortMainActivity.this).put("shakefilter",spinner_shakefilter.isChecked());


                    Log.d(TAG,"打开串口 ");
                }else{
                    Log.d(TAG,"关闭串口");
                    if(serialPortService!=null){
                        serialPortService.close();
                        serialPortService=null;
                    }

                }
            }
        });


    }


    public long getTimeOut(){
        String  outTime=(getResources().getStringArray(R.array.timeout)[spinner_timeout.getSelectedItemPosition()]);

        if(outTime.equals("不超时")){
            return 0;
        }
        return Integer.valueOf(outTime);


    }

    List<SerialPortCmdData> list;
    public void listView(){
        list= DBTableHelper.getInstance().selectSerialPortCmdData();
        if(list==null){
            return;
        }

        ListAdapter listAdapter=new ListAdapter(SerialPortMainActivity.this,list);
        list_collect.setAdapter(listAdapter);
        list_collect.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                edit_cmd.setText(""+list.get(position).cmd);
            }
        });

    }


    //发送串口命令
    public void sendCmd(View view) {

        String cmd="";
        String editCmd=edit_cmd.getText().toString().trim();
        if(editCmd.isEmpty()){
            return;
        }

        //16进制
        if(radio_sendhex.isChecked()){
            cmd=editCmd;
        }else      if(radio_sendascll.isChecked()){   //ascll

            cmd=ByteStringUtil.strToAscll(editCmd);

        }else      if(radio_sendtext.isChecked()){    //10进制
            cmd=editCmd;
        }

        if(serialPortService!=null){
            serialPortService.sendData(cmd);


        }




    }

    StringBuffer sb=new StringBuffer();

    @MainThread
    public void setResult(final String cmd){

        serial_result.post(new Runnable() {
            @Override
            public void run() {
                if(!cmd.isEmpty()){
                    sb.append(cmd);
                    sb.append("\r\n");
                }

                scroll_view.fullScroll(ScrollView.FOCUS_DOWN);
                serial_result.setText(""+sb.toString());
            }
        });


    }

    public void clean(View view) {
        sb.delete(0,sb.length());
        setResult("");
    }

    //命令收藏
    public void cmdColle(View view) {

        String cmd=edit_cmd.getText().toString();
        if(cmd.isEmpty()){
            return;
        }
        DBTableHelper.getInstance().insertSerialPortCmdData(cmd);

        listView();

    }
}
