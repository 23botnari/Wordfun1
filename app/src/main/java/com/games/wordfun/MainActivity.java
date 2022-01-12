package com.games.wordfun;

import android.content.Intent;
import android.media.MediaPlayer;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView txt_credit;
    private MediaPlayer mediaPlayer1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mediaPlayer1 = MediaPlayer.create(this, R.raw.button_click);
        txt_credit = (TextView) findViewById(R.id.txt_credit);
        txt_credit.setText(WApp.getDataStorage().getCredit() + "");

        findViewById(R.id.img_play).setOnClickListener(this);
        findViewById(R.id.img_setting).setOnClickListener(this);
        findViewById(R.id.btn_achieve).setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_play:
                if (WApp.getDataStorage().getSound()) {
                    if (mediaPlayer1 != null) {
                        mediaPlayer1.start();
                    }
                }
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.img_setting:
                if (WApp.getDataStorage().getSound()) {
                    if (mediaPlayer1 != null) {
                        mediaPlayer1.start();
                    }
                }

                Settings settings = new Settings(MainActivity.this, R.style.DialogTheme);
                settings.show();
                break;

            case R.id.btn_achieve:
                if (WApp.getDataStorage().getSound()) {
                    if (mediaPlayer1 != null) {
                        mediaPlayer1.start();
                    }
                }
                Intent intent1 = new Intent(MainActivity.this, AchieveActivity.class);
                startActivity(intent1);
                finish();
                break;

        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        txt_credit.setText(WApp.getDataStorage().getCredit() + "");
    }
}
