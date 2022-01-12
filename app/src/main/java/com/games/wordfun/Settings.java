package com.games.wordfun;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class Settings extends Dialog implements View.OnClickListener {
    private Context mcontext;

    public Settings(@NonNull Context context) {
        super(context);
        mcontext = context;
    }

    public Settings(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mcontext = context;
    }

    protected Settings(@NonNull Context context, boolean cancelable, @Nullable DialogInterface.OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        mcontext = context;
    }

    private MediaPlayer mediaPlayer1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_setting);
        setCancelable(true);

        mediaPlayer1 = MediaPlayer.create(mcontext, R.raw.button_click);

        final ImageView switch_music = (ImageView) findViewById(R.id.switch_music);
        final ImageView switch_sound = (ImageView) findViewById(R.id.switch_sound);

        if (!WApp.getDataStorage().getMusic()) {
            switch_music.setImageResource(R.drawable.switch_uncheck);
        } else {
            switch_music.setImageResource(R.drawable.switch_check);
        }
        if (!WApp.getDataStorage().getSound()) {
            switch_sound.setImageResource(R.drawable.switch_uncheck);
        } else {
            switch_sound.setImageResource(R.drawable.switch_check);
        }
        switch_music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (WApp.getDataStorage().getMusic()) {
                    WApp.getDataStorage().setMusic(false);
                    switch_music.setImageResource(R.drawable.switch_uncheck);

                } else {
                    WApp.getDataStorage().setMusic(true);
                    switch_music.setImageResource(R.drawable.switch_check);
                }
            }
        });

        switch_sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (WApp.getDataStorage().getSound()) {
                    WApp.getDataStorage().setSound(false);
                    switch_sound.setImageResource(R.drawable.switch_uncheck);
                } else {
                    WApp.getDataStorage().setSound(true);
                    switch_sound.setImageResource(R.drawable.switch_check);
                }
            }
        });

        findViewById(R.id.btn_rate).setOnClickListener(this);
        findViewById(R.id.btn_contact).setOnClickListener(this);
        findViewById(R.id.btn_privacy).setOnClickListener(this);
        findViewById(R.id.btn_close).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_rate:
                if (WApp.getDataStorage().getSound()) {
                    if (mediaPlayer1 != null) {
                        mediaPlayer1.start();
                    }
                }
                rateApp();
                break;
            case R.id.btn_contact:
                if (WApp.getDataStorage().getSound()) {
                    if (mediaPlayer1 != null) {
                        mediaPlayer1.start();
                    }
                }
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "contact__@gmail.com", null));
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Word Fun");

                try {
                    mcontext.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                    Log.i("sending email...", "finished");
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(mcontext, "There is no email client installed.", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.btn_privacy:
                if (WApp.getDataStorage().getSound()) {
                    if (mediaPlayer1 != null) {
                        mediaPlayer1.start();
                    }
                }

                WApp.viewUrl(mcontext,"privacy_policy.html");

                break;
            case R.id.btn_close:
                if (WApp.getDataStorage().getSound()) {
                    if (mediaPlayer1 != null) {
                        mediaPlayer1.start();
                    }
                }
                dismiss();
                break;
        }
    }

    public void rateApp() {
        Uri uri = Uri.parse("market://details?id=" + mcontext.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_DOCUMENT | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            mcontext.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            mcontext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + mcontext.getPackageName())));
        }
    }


}
