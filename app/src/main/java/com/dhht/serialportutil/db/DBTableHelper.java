package com.dhht.serialportutil.db;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.NameAlias;
import com.raizlabs.android.dbflow.sql.language.OrderBy;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.ModelAdapter;

import java.util.List;


/**
 * @author kcd
 * @package tld.fjsoft.com.shikeruibaserobot.db.utils
 * @filename TaskTableUtil
 * @date 2018/9/14 0014 16:15
 * @email 891705265@qq.com
 **/

public class DBTableHelper {
    String TAG = "DBTableHelper";
    private static DBTableHelper mTaskTableUtil;

    public static DBTableHelper getInstance() {
        if (mTaskTableUtil == null) {
            mTaskTableUtil = new DBTableHelper();
        }
        return mTaskTableUtil;
    }


    private ModelAdapter<SerialPortCmdData> getUserModelAdapter() {
        return FlowManager.getModelAdapter(SerialPortCmdData.class);
    }

    /**
     *添加数据
     */
    public boolean insertSerialPortCmdData(String cmd) {//账号为未删除、账号不一致才能添加

        SerialPortCmdData mSerialPortCmdData=new  SerialPortCmdData();
        mSerialPortCmdData.cmd=cmd;
        mSerialPortCmdData.createDateTimeMills=System.currentTimeMillis();

        getUserModelAdapter().insert(mSerialPortCmdData);

        return true;
    }

    /**
     * 通过id查询是否有数据
     *
     * @return
     */
    public List<SerialPortCmdData> selectSerialPortCmdData() {
        //先查询  .orderBy(OrderBy.fromNameAlias(NameAlias.of("id")))
        List<SerialPortCmdData> tempEleLockLogData = SQLite.select().from(SerialPortCmdData.class)
                .orderBy(SerialPortCmdData_Table.createDateTimeMills,false ).queryList();
        return tempEleLockLogData;

    }


    /**
     * 删除
     */
    public void delSerialPortCmdData(long id) {
        SQLite.delete(SerialPortCmdData.class)
                .where(SerialPortCmdData_Table.id.eq(id)).execute();

    }




}
