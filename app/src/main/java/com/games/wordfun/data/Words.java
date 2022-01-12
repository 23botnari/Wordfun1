package com.games.wordfun.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Words {
    @SerializedName("level_id")
    @Expose
    private Integer levelId;
    @SerializedName("phrase")
    @Expose
    private String phrase;
    @SerializedName("word1")
    @Expose
    private String word1;
    @SerializedName("word2")
    @Expose
    private String word2;
    @SerializedName("word3")
    @Expose
    private String word3;
    @SerializedName("word4")
    @Expose
    private String word4;

    public Integer getLevelId() {
        return levelId;
    }

    public String getPhrase() {
        return phrase;
    }

    public String getWord1() {
        return word1;
    }

    public String getWord2() {
        return word2;
    }

    public String getWord3() {
        return word3;
    }

    public String getWord4() {
        return word4;
    }


}
