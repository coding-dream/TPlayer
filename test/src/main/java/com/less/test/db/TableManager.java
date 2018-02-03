package com.less.test.db;

import android.database.sqlite.SQLiteDatabase;
import android.text.Html;

/**
 * Created by deeper on 2018/2/3.
 */

public class TableManager {

    public static void createTable(SQLiteDatabase db) {
        db.execSQL("create table if not exists " + Html.class.getSimpleName() + "(id integer primary key autoincrement, url varchar(255) not null unique, html text)");
    }

    public static void dropTable(SQLiteDatabase db) {
        db.execSQL("drop table if exists " + Html.class.getSimpleName());
    }
}
