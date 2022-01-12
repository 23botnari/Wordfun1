package com.games.wordfun.data;


import android.content.Context;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static DBHelper instance;
    public static final String DATABASE_NAME = "wordscore.db";
    public static final int DATABASE_VERSION = 1;
    private static final String TEXT_TYPE = " INTEGER";
    public static final String TABLE_NAME = "score";
    public static final String COLUMN_NAME = "credit";

    private static final String SQL_CREATE = "CREATE TABLE " + TABLE_NAME + " (" + COLUMN_NAME + TEXT_TYPE + " )";

    public static final String SQL_DELETE_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public static final String SQL_DELETE_ENTRIES =
            "DELETE FROM " + TABLE_NAME;


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DBHelper getinstance(Context context) {
        if (instance==null)
        {
            instance = new DBHelper(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_TABLE);
        onCreate(db);
    }
}
