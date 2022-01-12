package com.games.wordfun;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.games.wordfun.data.DBHelper;
import com.games.wordfun.data.DataStorage;
import com.games.wordfun.data.Words;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;

public class WApp extends Application {

    private static WApp instance;
    private static DataStorage dataStorage;
    private static List<Words> lst_words1, lst_words2, lst_words3;

    public static DataStorage getDataStorage() {
        return dataStorage;
    }

    public static List<Words> getWordsList1() {
        return lst_words1;
    }

    public static List<Words> getWordsList2() {
        return lst_words2;
    }

    public static List<Words> getWordsList3() {
        return lst_words3;
    }

    public static WApp getAppInstance() {
        return instance;
    }


    public static void viewUrl(Context context, String s) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse(s));
        String title = "Select Browser";
        Intent chooser = Intent.createChooser(intent, title);
        context.startActivity(chooser);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        dataStorage = new DataStorage(this);
        SQLiteDatabase.loadLibs(this);
        String data1 = loadJSONFromAsset1();
        Type type1 = new TypeToken<List<Words>>() {
        }.getType();
        lst_words1 = new Gson().fromJson(data1, type1);

        String data2 = loadJSONFromAsset2();
        Type type2 = new TypeToken<List<Words>>() {
        }.getType();
        lst_words2 = new Gson().fromJson(data2, type2);

        String data3 = loadJSONFromAsset3();
        Type type3 = new TypeToken<List<Words>>() {
        }.getType();
        lst_words3 = new Gson().fromJson(data3, type3);

        if (WApp.getDataStorage().isFirst()) {
            insertCreditToDb(500);
            WApp.getDataStorage().setFirst(false);
            WApp.getDataStorage().setCredit(500);
        }
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });

    }


    public String loadJSONFromAsset1() {
        String json = null;
        try {
            InputStream is = instance.getAssets().open("beginner.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }


    public String loadJSONFromAsset2() {
        String json = null;
        try {
            InputStream is = instance.getAssets().open("intermediate.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public String loadJSONFromAsset3() {
        String json = null;
        try {
            InputStream is = instance.getAssets().open("expert.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public void insertCreditToDb(int credit) {
        SQLiteDatabase db = DBHelper.getinstance(getAppInstance()).getWritableDatabase("gamewordfun");
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_NAME, credit);
        db.insert(DBHelper.TABLE_NAME, null, values);
        db.close();
    }

    public int getCreditFromDb() {
        SQLiteDatabase db = DBHelper.getinstance(getAppInstance()).getReadableDatabase("gamewordfun");
        Cursor cursor = db.rawQuery("SELECT * FROM '" + DBHelper.TABLE_NAME + "';", null);
        int credit = 0;

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    credit = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_NAME));
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        db.close();
        return credit;
    }

}
