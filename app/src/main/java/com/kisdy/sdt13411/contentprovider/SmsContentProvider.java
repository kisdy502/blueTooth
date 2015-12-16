package com.kisdy.sdt13411.contentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.kisdy.sdt13411.db.SmsDbOpenHelper;

/**
 * Created by sdt13411 on 2015/12/8.
 */
public class SmsContentProvider extends ContentProvider {

    private static final String AUTHORITY = "com.kisdy.sdt13411.contentprovider.SmsContentProvider";

    public static final Uri URI_SMS_ALL = Uri.parse("content://" + AUTHORITY + "/" + SmsDbOpenHelper.TABLE_SMS);
    private static final String  TAG ="SmsContentProvider" ;

    private static UriMatcher matcher;

    private static final int SMS_ALL = 0;
    private static final int SMS_ONE = 1;
    private static final int SMS_INSERT = 2;
    //private static final int SMS_UPDATE = 3;
    //private static final int SMS_DELETE = 4;


    static {
        matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(AUTHORITY, SmsDbOpenHelper.TABLE_SMS , SMS_ALL);
        matcher.addURI(AUTHORITY, SmsDbOpenHelper.TABLE_SMS + "/#", SMS_ONE);
        //matcher.addURI(AUTHORITY, SmsDbOpenHelper.TABLE_SMS + "/insert", SMS_INSERT);
    }


    SQLiteDatabase mDbBase;
    SQLiteOpenHelper mHelper;


    @Override
    public boolean onCreate() {
        mHelper = new SmsDbOpenHelper(getContext());

        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        /* if (mDbBase.isOpen()) {
                Cursor cursor = mDbBase.query(SmsDbOpenHelper.TABLE_SMS, projection, selection, selectionArgs, null, null, sortOrder);
                return cursor;
            }*/
        switch (matcher.match(uri)) {
            case SMS_ALL:
                break;
            case SMS_ONE:
                if (mDbBase.isOpen()) {
                    long id = ContentUris.parseId(uri);
                    selection="_id = ?";
                    selectionArgs=new String[]{id + ""};
                }
            default:
                throw new IllegalArgumentException("URI不匹配: " + uri);
        }
        mDbBase = mHelper.getWritableDatabase();
        Cursor cursor = mDbBase.query(SmsDbOpenHelper.TABLE_SMS, projection,selection , selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(),URI_SMS_ALL);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        switch (matcher.match(uri)) {
            case SMS_ALL:
                mDbBase = mHelper.getWritableDatabase();
                long id = mDbBase.insert("sms", null, values);
                if (id > 0) {
                    Log.d(TAG,"id="+id);
                    getContext().getContentResolver().notifyChange(URI_SMS_ALL, null);
                    return ContentUris.withAppendedId(uri, id);
                }
                break;
            default:
                throw new IllegalArgumentException("URI不匹配: " + uri);
        }
        return null;

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
