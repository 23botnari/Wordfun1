package com.games.wordfun;

import android.content.Intent;
import android.media.MediaPlayer;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

public class AchieveActivity extends AppCompatActivity implements View.OnClickListener {

    private SeekBar sb_basic, sb_inter, sb_expert;
    private TextView txt_basic, txt_expert, txt_inter;
    private static MediaPlayer mediaPlayer1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achieve);

        mediaPlayer1 = MediaPlayer.create(this, R.raw.button_click);
        sb_basic = (SeekBar) findViewById(R.id.sb_basic);
        sb_inter = (SeekBar) findViewById(R.id.sb_inter);
        sb_expert = (SeekBar) findViewById(R.id.sb_expert);
        txt_basic = (TextView) findViewById(R.id.txt_basic);
        txt_expert = (TextView) findViewById(R.id.txt_expert);
        txt_inter = (TextView) findViewById(R.id.txt_inter);

        sb_basic.setEnabled(false);
        sb_expert.setEnabled(false);
        sb_inter.setEnabled(false);

        if (WApp.getDataStorage().getSetting_Level() <= 50) {
            sb_basic.setProgress(WApp.getDataStorage().getSetting_Level());
            sb_inter.setProgress(0);
            sb_expert.setProgress(0);
            txt_basic.setText(String.format("%d/50", WApp.getDataStorage().getSetting_Level()));
            txt_inter.setText(String.format("%d/50", 0));
            txt_expert.setText(String.format("%d/50", 0));
        }
        if (WApp.getDataStorage().getSetting_Level() > 50 && WApp.getDataStorage().getSetting_Level() <= 100) {
            sb_inter.setProgress(WApp.getDataStorage().getSetting_Level()-50);
            sb_basic.setProgress(50);
            sb_expert.setProgress(0);
            txt_inter.setText(String.format("%d/50", WApp.getDataStorage().getSetting_Level()-50));
            txt_basic.setText(String.format("%d/50", 50));
            txt_expert.setText(String.format("%d/50", 0));
        }
        if (WApp.getDataStorage().getSetting_Level() > 100 && WApp.getDataStorage().getSetting_Level() <= 150) {
            sb_expert.setProgress(WApp.getDataStorage().getSetting_Level()-100);
            sb_basic.setProgress(50);
            sb_inter.setProgress(50);
            txt_inter.setText(String.format("%d/50", 50));
            txt_basic.setText(String.format("%d/50", 50));
            txt_expert.setText(String.format("%d/50", WApp.getDataStorage().getSetting_Level()-100));
        }
        findViewById(R.id.img_back).setOnClickListener(this);
        findViewById(R.id.img_basic).setOnClickListener(this);
        findViewById(R.id.img_expert).setOnClickListener(this);
        findViewById(R.id.img_inter).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                if (WApp.getDataStorage().getSound()) {
                    if (mediaPlayer1 != null) {
                        mediaPlayer1.start();
                    }
                }
                Intent intent = new Intent(AchieveActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.img_basic:
                if (WApp.getDataStorage().getSound()) {
                    if (mediaPlayer1 != null) {
                        mediaPlayer1.start();
                    }
                }
                Intent intent1 = new Intent(AchieveActivity.this, BasicActivity.class);
                startActivity(intent1);
                finish();
                break;
            case R.id.img_expert:
                if (WApp.getDataStorage().getSound()) {
                    if (mediaPlayer1 != null) {
                        mediaPlayer1.start();
                    }
                }
                Intent intent2 = new Intent(AchieveActivity.this, ExpertActivity.class);
                startActivity(intent2);
                finish();
                break;
            case R.id.img_inter:
                if (WApp.getDataStorage().getSound()) {
                    if (mediaPlayer1 != null) {
                        mediaPlayer1.start();
                    }
                }
                Intent intent3 = new Intent(AchieveActivity.this, InterActivity.class);
                startActivity(intent3);
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent1 = new Intent(AchieveActivity.this, MainActivity.class);
        startActivity(intent1);
        finish();
    }
}
