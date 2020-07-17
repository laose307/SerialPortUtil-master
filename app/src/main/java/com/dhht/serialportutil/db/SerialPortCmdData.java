package com.dhht.serialportutil.db;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.io.Serializable;

/**
 * 电子柜信息
 * 缓存电子柜信息，可能会涉及到全柜打开，就要去查询柜子数据库，逐一下发串口
 * @author kcd
 * @package tld.fjsoft.com.shikeruibaserobot.db
 * @filename Good
 * @date 2018/9/14 0014 14:51
 * @email 891705265@qq.com
 **/
@Table(database = AppDatabase.class)
public class SerialPortCmdData extends BaseModel implements Serializable {

    @Column
    @PrimaryKey(autoincrement = true)
    public long id;
    @Column
    public String cmd;

    @Column
    public long createDateTimeMills;

}
