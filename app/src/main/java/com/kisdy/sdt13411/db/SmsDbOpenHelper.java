package com.kisdy.sdt13411.db;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

import com.kisdy.sdt13411.bean.SendedMsg;
import com.kisdy.sdt13411.contentprovider.SmsContentProvider;

import java.util.Date;

/**
 * Created by sdt13411 on 2015/12/8.
 */
public class SmsDbOpenHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "sdt_sms.db";  //数据库名
    public static final int DB_VERSION = 1;

    public static final String TABLE_SMS = "sms"; //表名
    private static final String TAG = "SmsDbOpenHelper";

    public SmsDbOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public SmsDbOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String strSql = "create table " + TABLE_SMS + "("
                + SmsItem.FILED_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + SmsItem.FILED_SMSCONTENT + " TEXT,"
                + SmsItem.FILED_CONTACTNAMES + " TEXT,"
                + SmsItem.FILED_CONTACTNUMBERS + " TEXT,"
                + SmsItem.FILED_FESTIVALENAME + " TEXT,"
                + SmsItem.FILED_SENDEDDATE + " TEXT );";
        db.execSQL(strSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static final class SmsItem implements BaseColumns {

        public static final String FILED_ID = "_id";

        public static final String FILED_SMSCONTENT = "smscontent";

        public static final String FILED_CONTACTNAMES = "contactnames";

        public static final String FILED_CONTACTNUMBERS = "contactnumbers";

        public static final String FILED_SENDEDDATE = "send_date";

        public static final String FILED_FESTIVALENAME = "festivalename";
    }


    public static void insertSendedMsg(Context context, SendedMsg msg) {

        // 内容提供者访问对象
        ContentResolver contentresolver = context.getContentResolver();

        ContentValues values = new ContentValues();

        //TODO  这里需要处理SendedMsg转ContentValues
        msg.setDate(new Date());
        values.put(SmsItem.FILED_SMSCONTENT, msg.getSmsContent());
        values.put(SmsItem.FILED_CONTACTNAMES, msg.getNames());
        values.put(SmsItem.FILED_CONTACTNUMBERS, msg.getNumber());
        values.put(SmsItem.FILED_SENDEDDATE, msg.getDateDesc());
        values.put(SmsItem.FILED_FESTIVALENAME, msg.getFestivalName());


        Uri uri = contentresolver.insert(SmsContentProvider.URI_SMS_ALL, values);
        Log.i(TAG, "uri: " + uri);
        long id = ContentUris.parseId(uri);
        Log.i(TAG, "添加到: " + id);
    }
}
