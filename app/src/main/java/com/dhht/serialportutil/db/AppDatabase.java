package com.dhht.serialportutil.db;

import com.raizlabs.android.dbflow.annotation.Database;

@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION)
public class AppDatabase {
    //数据库名称
    public static final String NAME = "serialprtdata";
    //数据库版本号
    public static final int VERSION = 1;

}
