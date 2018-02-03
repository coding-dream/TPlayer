package com.less.test.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.less.test.AppConfig;
import com.less.test.db.TableManager;

/**
 * Created by deeper on 2018/2/3.
 */

public class SQLHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = AppConfig.DB_NAME;
    private static final int DB_VERSION = AppConfig.DB_VERSION;

    public SQLHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        TableManager.createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        TableManager.dropTable(db);
        TableManager.createTable(db);
    }
}