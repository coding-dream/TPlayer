package com.less.test.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.less.test.bean.Html;
import com.less.test.util.SQLHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by deeper on 2018/2/3.
 */

public class HtmlDaoImpl implements HtmlDao {

    private static final String TABLE_NAME = Html.class.getSimpleName();
    private static final String TAG = HtmlDaoImpl.class.getSimpleName();

    private SQLHelper mHelper;

    public HtmlDaoImpl(Context context){
        mHelper = new SQLHelper(context);
    }

    @Override
    public void save(Html html) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("insert into "
                        + TABLE_NAME
                        + "(url,html) values(?, ?)",
                new Object[]{html.getUrl(), html.getHtml()});
        Log.d(TAG, "save success: " + html.getUrl());
    }

    @Override
    public List search(String text) {
        String query = "%" + text + "%";
        String sql = String.format("select * from " + TABLE_NAME + " where html like '%s'", query);
        List<Html> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sql,null);
        while (cursor.moveToNext()) {
            Html html = new Html();
            html.setUrl(cursor.getString(cursor.getColumnIndex("url")));
            html.setHtml(cursor.getString(cursor.getColumnIndex("html")));
            list.add(html);
        }
        cursor.close();
        return list;
    }

    @Override
    public int count() {
        String sql = String.format("select count(*) as count from %s", TABLE_NAME);
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        boolean exist = cursor.moveToNext();
        if (exist) {
            return cursor.getInt(cursor.getColumnIndex("count"));
        }
        cursor.close();
        return 0;
    }

    protected SQLiteDatabase getWritableDatabase() {
        return mHelper.getWritableDatabase();
    }

    protected SQLiteDatabase getReadableDatabase() {
        return mHelper.getReadableDatabase();
    }

    public void close() {
        mHelper.close();
    }
}
