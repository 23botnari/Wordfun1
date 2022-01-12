package com.games.wordfun.data;

import android.content.Context;
import android.content.SharedPreferences;

public class DataStorage {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public DataStorage(Context context) {
        sharedPreferences = context.getSharedPreferences("word_fun", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setLevel(Integer level) {
        editor.putInt("Level", level);
        editor.apply();
    }

    public Integer getLevel() {
        return sharedPreferences.getInt("Level", 0);
    }

    public void setLevelCount(Integer levelCount) {
        editor.putInt("LevelCount", levelCount);
        editor.apply();
    }

    public Integer getLevelCount() {
        return sharedPreferences.getInt("LevelCount", 0);
    }

    public void setCredit(Integer credit) {
        editor.putInt("Credit", credit);
        editor.apply();

    }

    public Integer getCredit() {
//        WApp.getAppInstance().getCreditFromDb();
        return sharedPreferences.getInt("Credit", 0);
    }

    public void setSound(boolean sound) {
        editor.putBoolean("Sound", sound);
        editor.apply();
    }

    public boolean getSound() {
        return sharedPreferences.getBoolean("Sound", true);
    }


    public void setMusic(boolean music) {
        editor.putBoolean("Music", music);
        editor.apply();
    }

    public boolean getMusic() {
        return sharedPreferences.getBoolean("Music", true);
    }

    public void setSetting_Level(Integer setting_level) {
        editor.putInt("Setting_Level", setting_level);
        editor.apply();
    }

    public Integer getSetting_Level() {
        return sharedPreferences.getInt("Setting_Level", 0);
    }

    public boolean isFirst()
    {
        return sharedPreferences.getBoolean("first",true);
    }

    public void setFirst(boolean first)
    {
        editor.putBoolean("first",first);
        editor.apply();
    }

    public void clearAll() {
        editor.clear();
        editor.apply();
        editor.commit();
    }
}
