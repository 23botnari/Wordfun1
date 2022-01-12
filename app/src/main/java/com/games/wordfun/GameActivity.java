package com.games.wordfun;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.plattysoft.leonids.ParticleSystem;
import com.games.wordfun.view.CircularLayout;

import java.util.ArrayList;
import java.util.Collections;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    private static int count = 0;
    private static Context context;
    static ArrayList<String> itemTitles;
    private static CircularLayout circular_layout;
    private static char[] a;

    static ArrayList<String> gen_words = new ArrayList<>();
    private static MediaPlayer mediaPlayer, mediaPlayer1;

    public static LinearLayout ll_top, ll_middle;
    private static LinearLayout linearLayout;
    public static int level, hint_count = 0, level_count;
    private static ArrayList<String> already = new ArrayList<>();
    private static ArrayList<String> hint = new ArrayList<>();
    private static ArrayList<String> ch_hint = new ArrayList<>();
    private static Animation animShake;
    private static RelativeLayout rl_main;
    private static TextView txt_level, txt_credit;
    private static float[] angle_3 = new float[]{15, -15, 15};
    private static float[] angle_4 = new float[]{15, -15, 15, -15};
    private static float[] angle_5 = new float[]{15, -15, 15, -15, 15};
    private static float[] angle_6 = new float[]{15, -15, 15, -15, 15, -15};

    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        context = GameActivity.this;
        itemTitles = new ArrayList<>();


        mediaPlayer1 = MediaPlayer.create(this, R.raw.button_click);

        animShake = AnimationUtils.loadAnimation(this, R.anim.shake);
        ll_top = (LinearLayout) findViewById(R.id.ll_top);
        circular_layout = (CircularLayout) findViewById(R.id.circular_layout);
        rl_main = (RelativeLayout) findViewById(R.id.rl_main);
        ll_middle = (LinearLayout) findViewById(R.id.ll_middle);
        txt_level = (TextView) findViewById(R.id.txt_level);
        txt_credit = (TextView) findViewById(R.id.txt_credit);

        generateNewWord();

        findViewById(R.id.btn_shuffle).setOnClickListener(this);
        findViewById(R.id.btn_hint).setOnClickListener(this);
        findViewById(R.id.btn_rate).setOnClickListener(this);
        findViewById(R.id.btn_ad).setOnClickListener(this);
        findViewById(R.id.btn_achieve).setOnClickListener(this);
        findViewById(R.id.btn_setting).setOnClickListener(this);
        adMobLoad();
    }

    public void adMobLoad(){
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this,getString(R.string.admob_interstitial_ad), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                mInterstitialAd = interstitialAd;
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error
                mInterstitialAd = null;
            }
        });
    }
    public static void generateNewWord() {

        if (WApp.getDataStorage().getMusic()) {
            playMusic();
        } else {
            stopMusic();
        }

        ll_top.removeAllViews();
        count = 0;
        itemTitles.clear();
        gen_words.clear();
        already.clear();
        hint.clear();
        String w1, w2, w3, w4;
        level = WApp.getDataStorage().getLevel();

        level_count = WApp.getDataStorage().getLevelCount();

        if (level_count >= 50) {
            WApp.getDataStorage().setLevelCount(0);
            level_count = WApp.getDataStorage().getLevelCount();
        }

        if (level >= 0 && level < 50) {
            a = WApp.getWordsList1().get(level_count).getPhrase().toCharArray();

            w1 = WApp.getWordsList1().get(level_count).getWord1();
            w2 = WApp.getWordsList1().get(level_count).getWord2();
            w3 = WApp.getWordsList1().get(level_count).getWord3();
            w4 = WApp.getWordsList1().get(level_count).getWord4();
        } else if (level >= 50 && level < 100) {
            a = WApp.getWordsList2().get(level_count).getPhrase().toCharArray();

            w1 = WApp.getWordsList2().get(level_count).getWord1();
            w2 = WApp.getWordsList2().get(level_count).getWord2();
            w3 = WApp.getWordsList2().get(level_count).getWord3();
            w4 = WApp.getWordsList2().get(level_count).getWord4();
        } else if (level >= 100 && level < 150) {
            a = WApp.getWordsList3().get(level_count).getPhrase().toCharArray();

            w1 = WApp.getWordsList3().get(level_count).getWord1();
            w2 = WApp.getWordsList3().get(level_count).getWord2();
            w3 = WApp.getWordsList3().get(level_count).getWord3();
            w4 = WApp.getWordsList3().get(level_count).getWord4();
        } else {
            WApp.getDataStorage().setLevel(0);
            level = WApp.getDataStorage().getLevel();

            a = WApp.getWordsList1().get(level_count).getPhrase().toCharArray();
            w1 = WApp.getWordsList1().get(level_count).getWord1();
            w2 = WApp.getWordsList1().get(level_count).getWord2();
            w3 = WApp.getWordsList1().get(level_count).getWord3();
            w4 = WApp.getWordsList1().get(level_count).getWord4();
        }

        txt_level.setText(String.format(context.getString(R.string.level), level_count + 1));
        updateCreadit();

//        a = words[level].toCharArray();
        if (!w1.equalsIgnoreCase("")) {
            gen_words.add(w1);
        }
        if (!w2.equalsIgnoreCase("")) {
            gen_words.add(w2);
        }

        if (!w3.equalsIgnoreCase("")) {
            gen_words.add(w3);
        }
        if (!w4.equalsIgnoreCase("")) {
            gen_words.add(w4);
        }

        for (int i = 0; i < a.length; i++) {
            itemTitles.add(String.valueOf(a[i]));
        }

        for (int i = 0; i < gen_words.size(); i++) {
            linearLayout = new LinearLayout(context);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
//            lp.setMargins(0, 10, 0, 10);
            linearLayout.setLayoutParams(lp);
            int size = gen_words.get(i).length();
            for (int j = 0; j < size; j++) {
                TextView textView = new TextView(context);
                textView.setAllCaps(true);
                LinearLayout.LayoutParams layoutParams;

                layoutParams = new LinearLayout.LayoutParams(context.getResources().getDimensionPixelSize(R.dimen.word_big), context.getResources().getDimensionPixelSize(R.dimen.word_big));

                layoutParams.setMargins(10, 10, 10, 10);
                textView.setLayoutParams(layoutParams);

                textView.setBackground(context.getResources().getDrawable(R.drawable.box_empty));
                linearLayout.addView(textView);
            }
            ll_top.addView(linearLayout);
        }

        circular_layout.setCapacity(a.length);

//        ic_shuffle();
        circular_layout.removeAllViews();
        for (int i = 0; i < a.length; i++) {
            final TextView textView = (TextView) LayoutInflater.from(context).inflate(R.layout.number_text_view, null);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(context.getResources().getDimensionPixelSize(R.dimen.word_big), context.getResources().getDimensionPixelSize(R.dimen.word_big));
            layoutParams.setMargins(10, 10, 10, 10);

            textView.setLayoutParams(layoutParams);
            textView.setText(itemTitles.get(i));
            textView.setAllCaps(true);
            textView.startAnimation(animShake);

            switch (a.length) {
                case 3:
                    textView.setRotation(angle_3[i]);
                    break;
                case 4:
                    textView.setRotation(angle_4[i]);
                    break;
                case 5:
                    textView.setRotation(angle_5[i]);
                    break;
                case 6:
                    textView.setRotation(angle_6[i]);
                    break;
            }
            circular_layout.addView(textView);
        }
    }

    public static void shuffle() {
        Collections.shuffle(itemTitles);

        circular_layout.removeAllViews();
        for (int i = 0; i < a.length; i++) {
            final TextView textView = (TextView) LayoutInflater.from(context).inflate(R.layout.number_text_view, null);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(context.getResources().getDimensionPixelSize(R.dimen.word_big), context.getResources().getDimensionPixelSize(R.dimen.word_big));
            layoutParams.setMargins(10, 10, 10, 10);
            textView.setLayoutParams(layoutParams);
            textView.setText(itemTitles.get(i));
            textView.setAllCaps(true);
            textView.startAnimation(animShake);

            switch (a.length) {
                case 3:
                    textView.setRotation(angle_3[i]);
                    break;
                case 4:
                    textView.setRotation(angle_4[i]);
                    break;
                case 5:
                    textView.setRotation(angle_5[i]);
                    break;
                case 6:
                    textView.setRotation(angle_6[i]);
                    break;
            }
            circular_layout.addView(textView);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_shuffle:

                if (WApp.getDataStorage().getSound()) {
                    playMusic1();
                }
                shuffle();
                break;

            case R.id.btn_hint:
                if (WApp.getDataStorage().getSound()) {
                    playMusic1();
                }

                hint();
                break;

            case R.id.btn_rate:
                if (WApp.getDataStorage().getSound()) {
                    playMusic1();
                }
                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_DOCUMENT | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                }

                break;

            case R.id.btn_ad:
                if (WApp.getDataStorage().getSound()) {
                    playMusic1();
                }
                watchVideoDialog();
                break;

            case R.id.btn_achieve:
                if (WApp.getDataStorage().getSound()) {
                    playMusic1();
                }
                Intent intent1 = new Intent(GameActivity.this, AchieveActivity.class);
                startActivity(intent1);
                finish();
                break;

            case R.id.btn_setting:
                if (WApp.getDataStorage().getSound()) {
                    playMusic1();
                }

                showSetttingDialog();
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
                    startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                    Log.i("sending email...", "finished");
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(context, "There is no email client installed.", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.btn_privacy:
                if (WApp.getDataStorage().getSound()) {
                    if (mediaPlayer1 != null) {
                        mediaPlayer1.start();
                    }
                }
                WApp.viewUrl(this,  "privacy_policy.html");
                break;
        }
    }

    public void showSetttingDialog() {

        final Dialog dialog = new Dialog(context, R.style.DialogTheme);
//        dialog.getWindow().setFlags(1024, 1024);
        dialog.setContentView(R.layout.dialog_setting);
        dialog.setCancelable(true);

        mediaPlayer1 = MediaPlayer.create(context, R.raw.button_click);

        final ImageView switch_music = (ImageView) dialog.findViewById(R.id.switch_music);
        final ImageView switch_sound = (ImageView) dialog.findViewById(R.id.switch_sound);

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

                    stopMusic();
                } else {
                    playMusic();
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

        dialog.findViewById(R.id.btn_rate).setOnClickListener(this);
        dialog.findViewById(R.id.btn_contact).setOnClickListener(this);
        dialog.findViewById(R.id.btn_privacy).setOnClickListener(this);
        dialog.findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (WApp.getDataStorage().getSound()) {
                    playMusic1();
                }
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    public static class increaseCredits extends AsyncTask<Integer, Integer, Void> {
        increaseCredits() {

        }

        @Override
        protected Void doInBackground(Integer... integers) {
            WApp.getAppInstance().insertCreditToDb(WApp.getAppInstance().getCreditFromDb() + integers[0]);
            return null;
        }
    }

    public static class decreaseCredits extends AsyncTask<Integer, Integer, Void> {
        decreaseCredits() {

        }

        @Override
        protected Void doInBackground(Integer... integers) {
            WApp.getAppInstance().insertCreditToDb(WApp.getAppInstance().getCreditFromDb() - integers[0]);
            return null;
        }
    }

    public  void hint() {
        if (WApp.getDataStorage().getCredit() >= 100) {

            WApp.getDataStorage().setCredit(WApp.getDataStorage().getCredit() - 100);
            updateCreadit();

            new decreaseCredits().execute(100);

            if (count < gen_words.size()) {
                for (int i = 0; i < gen_words.size(); i++) {
                    if (hint.isEmpty() || !hint.contains(gen_words.get(i))) {
                        ll_top.removeViewAt(i);
                        linearLayout = new LinearLayout(context);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
//                    lp.setMargins(0, 10, 0, 10);
                        linearLayout.setLayoutParams(lp);

                        int size = gen_words.get(i).length();
                        char a[] = gen_words.get(i).toCharArray();

                        if (hint_count >= size) {
                            hint.add(gen_words.get(i));
                            hint_count = 0;
                            ch_hint.clear();
                        }
                        if (hint_count < size) {
                            for (int j = 0; j < size; j++) {
                                final TextView textView = new TextView(context);
                                LinearLayout.LayoutParams layoutParams;

                                layoutParams = new LinearLayout.LayoutParams(context.getResources().getDimensionPixelSize(R.dimen.word_big), context.getResources().getDimensionPixelSize(R.dimen.word_big));

                                layoutParams.setMargins(10, 10, 10, 10);
                                textView.setLayoutParams(layoutParams);
                                textView.setAllCaps(true);

                                if (j <= hint_count) {
                                    textView.setText(a[j] + "");
                                    ch_hint.add(a[j] + "");
                                    textView.setBackground(context.getResources().getDrawable(R.drawable.box_word));
                                } else {
                                    textView.setText("");
                                    textView.setBackground(context.getResources().getDrawable(R.drawable.box_empty));
                                }
                                textView.setGravity(Gravity.CENTER);
                                textView.setTextSize(context.getResources().getDimensionPixelSize(R.dimen.text));
                                textView.setTextColor(context.getResources().getColor(R.color.txt_hint));
                                textView.setTypeface(textView.getTypeface(), Typeface.BOLD);

                                linearLayout.addView(textView, j);
                            }
                            hint_count++;

                            ll_top.addView(linearLayout, i);
                            StringBuilder s = new StringBuilder();
                            for (int j = 0; j < ch_hint.size(); j++) {
                                s.append(ch_hint.get(j));
                            }
                            ch_hint.clear();
                            if (s.toString().equalsIgnoreCase(gen_words.get(i))) {
                                hint.add(gen_words.get(i));
                                hint_count = 0;
                            }
                            break;
                        }
                    }
                }
            }
        } else {
            watchVideoDialog();
            /*HintPurchase hintPurchase = new HintPurchase(context, R.style.DialogTheme);
            hintPurchase.show();*/
        }
    }

    public static int type = 0;

    public static void textmatcher(final Context context, String s) {
        String w = s;
        if (already.contains(w)) {
            Toast.makeText(context, "Word already exist.\n Please try another.", Toast.LENGTH_SHORT).show();
        } else {
            for (int i = 0; i < gen_words.size(); i++) {
                if (!gen_words.contains(w)) {
                    type = 1;
                    Toast.makeText(context, "Selected word is wrong\nPlease try another word.", Toast.LENGTH_SHORT).show();
                }
                if (gen_words.get(i).equalsIgnoreCase(w)) {
                    if (type == 0) {
                        showPopup(i);
                    }
                    type = 0;
                    already.add(s);
                    hint.add(s);
                    count++;
                    ll_top.removeViewAt(i);
                    ch_hint.clear();
                    hint_count = 0;
                    linearLayout = new LinearLayout(context);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    linearLayout.setOrientation(LinearLayout.HORIZONTAL);
//                    lp.setMargins(0, 10, 0, 10);
                    linearLayout.setLayoutParams(lp);
                    int size = gen_words.get(i).length();
                    char a[] = gen_words.get(i).toCharArray();
                    for (int j = 0; j < size; j++) {
                        final TextView textView = new TextView(context);
                        textView.setBackground(context.getResources().getDrawable(R.drawable.box_word));

                        LinearLayout.LayoutParams layoutParams;

                        layoutParams = new LinearLayout.LayoutParams(context.getResources().getDimensionPixelSize(R.dimen.word_big), context.getResources().getDimensionPixelSize(R.dimen.word_big));

                        layoutParams.setMargins(10, 10, 10, 10);
                        textView.setLayoutParams(layoutParams);
                        textView.setText(a[j] + "");
                        textView.setAllCaps(true);
                        textView.setGravity(Gravity.CENTER);
                        textView.setTextSize(context.getResources().getDimensionPixelSize(R.dimen.text));
                        textView.setTextColor(context.getResources().getColor(R.color.light_brown));
                        textView.setTypeface(textView.getTypeface(), Typeface.BOLD);

                        textView.getViewTreeObserver().addOnGlobalLayoutListener(
                                new ViewTreeObserver.OnGlobalLayoutListener() {
                                    @SuppressWarnings("deprecation")
                                    @Override
                                    public void onGlobalLayout() {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                            textView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                                        } else {
                                            textView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                                        }

                                        new ParticleSystem((Activity) context, 50, R.drawable.star_yellow, 400)
                                                .setSpeedRange(0.1f, 0.15f)
                                                .setScaleRange(0.7f, 1.0f)
                                                .oneShot(textView, 30);
                                    }
                                });
                        linearLayout.addView(textView);
                    }
                    ll_top.addView(linearLayout, i);
                }
            }

            if (count >= gen_words.size()) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        windialog();
                    }
                }, 2500);
            }
        }
    }

    public static void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public static void playMusic() {
        stopMusic();
        mediaPlayer = MediaPlayer.create(context, R.raw.background);
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    public static void playMusic1() {
        if (mediaPlayer1 != null) {
            mediaPlayer1.start();
        }
    }

    public static void wordCatcher(final Context context, String s) {
        for (int i = 0; i < s.length(); i++) {
            ll_middle.removeAllViews();
            int size = s.length();
            char a[] = s.toCharArray();
            for (int j = 0; j < size; j++) {
                final TextView textView = new TextView(context);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(2, 2, 2, 2);
                textView.setLayoutParams(layoutParams);
                textView.setText(a[j] + "");
                textView.setAllCaps(true);
                textView.setGravity(Gravity.CENTER);
                textView.setTextSize(context.getResources().getDimensionPixelSize(R.dimen.text));
                textView.setTextColor(context.getResources().getColor(R.color.light_brown));
                textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
                ll_middle.addView(textView);
            }
        }
    }


    public static void windialog() {
        stopMusic();
        final Dialog dialog = new Dialog(context, R.style.DialogTheme);

        dialog.setContentView(R.layout.dialog_win);
        dialog.setCancelable(false);
        SeekBar sb_win = (SeekBar) dialog.findViewById(R.id.sb_win);
        sb_win.setEnabled(false);
        TextView txt_chptr_win = (TextView) dialog.findViewById(R.id.txt_chptr_win);

        txt_chptr_win.setText(String.format("LEVEL %d/50", WApp.getDataStorage().getLevelCount() + 1));
        sb_win.setProgress(WApp.getDataStorage().getLevelCount() + 1);
        final KonfettiView viewKonfetti = (KonfettiView) dialog.findViewById(R.id.viewKonfetti);

        viewKonfetti.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @SuppressWarnings("deprecation")
                    @Override
                    public void onGlobalLayout() {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            viewKonfetti.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        } else {
                            viewKonfetti.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        }

                        viewKonfetti.build()
                                .addColors(context.getResources().getColor(R.color.yellow), context.getResources().getColor(R.color.navy), context.getResources().getColor(R.color.orange))
                                .setDirection(0.0, 359.0)
                                .setSpeed(1f, 5f)
                                .setFadeOutEnabled(true)
                                .setTimeToLive(3000L)
                                .addShapes(Shape.RECT, Shape.CIRCLE)
                                .addSizes(new Size(10, 5))
                                .setPosition(-50f, viewKonfetti.getWidth() + 50f, -50f, -50f)
                                .streamFor(70, Integer.MAX_VALUE);
                    }
                });

        dialog.findViewById(R.id.btn_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                level_count++;

                if (WApp.getDataStorage().getSound()) {
                    playMusic1();
                }

                if (WApp.getDataStorage().getMusic()) {
                    playMusic();
                } else {
                    stopMusic();
                }

                int lvl = level + 1;
                WApp.getDataStorage().setLevel(lvl);
                WApp.getDataStorage().setLevelCount(level_count + 1);

                if (lvl > WApp.getDataStorage().getSetting_Level()) {
                    WApp.getDataStorage().setSetting_Level(lvl);
                    WApp.getDataStorage().setCredit(WApp.getDataStorage().getCredit() + 50);
                    new increaseCredits().execute(50);
                }

                gen_words.clear();
                generateNewWord();
                already.clear();
                hint.clear();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void watchVideoDialog() {

        final Dialog dialog = new Dialog(context, R.style.DialogTheme);

        dialog.setContentView(R.layout.dialog_video);
        dialog.setCancelable(true);

        dialog.findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (WApp.getDataStorage().getSound()) {
                    playMusic1();
                }
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.btn_getit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (WApp.getDataStorage().getSound()) {
                    playMusic1();
                }

                if (mInterstitialAd != null) {
                    mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            // Called when fullscreen content is dismissed.
                            if (WApp.getDataStorage().getMusic()) {
                                playMusic();
                            } else {
                                stopMusic();
                            }
                            WApp.getDataStorage().setCredit(WApp.getDataStorage().getCredit() + 100);
                            new increaseCredits().execute(100);

                            Log.d("TAG", "The ad was dismissed.");
                            adMobLoad();
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(AdError adError) {
                            // Called when fullscreen content failed to show.
                            Log.d("TAG", "The ad failed to show.");
                            adMobLoad();
                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            // Called when fullscreen content is shown.
                            // Make sure to set your reference to null so you don't
                            // show it a second time.
                            mInterstitialAd = null;
                            Log.d("TAG", "The ad was shown.");
                        }
                    });
                    mInterstitialAd.show(GameActivity.this);
                } else {
                    Log.d("TAG", "The interstitial ad wasn't ready yet.");
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static void getPointsDialog() {
        stopMusic();
        final Dialog dialog = new Dialog(context, R.style.DialogTheme);
        dialog.setContentView(R.layout.dialog_getpoints);
        dialog.setCancelable(false);

        dialog.findViewById(R.id.btn_getit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (WApp.getDataStorage().getMusic()) {
                    playMusic();
                } else {
                    stopMusic();
                }
                WApp.getDataStorage().setCredit(WApp.getDataStorage().getCredit() + 100);
                new increaseCredits().execute(100);

                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }

        Intent intent = new Intent(GameActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    public static void updateCreadit() {
        txt_credit.setText(WApp.getDataStorage().getCredit() + "");
    }

    @Override
    public void onResume() {
        super.onResume();

        if (WApp.getDataStorage().getMusic()) {
            playMusic();
        } else {
            stopMusic();
        }
        updateCreadit();
    }

    @Override
    public void onPause() {
        Log.e("Pause ", "music stopped");

        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onPause();

    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.e("Destroy ", "music stopped");
//        stopMusic();
    }


    public static void showPopup(int img) {
        int gravity = Gravity.CENTER;

        int[] images = new int[]{R.drawable.lbl_great, R.drawable.lbl_bravo, R.drawable.lbl_execellent, R.drawable.lbl_amazing};
        int[] sounds = new int[]{R.raw.great, R.raw.bravo, R.raw.excellent, R.raw.amazing};
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_window, null);

        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.MATCH_PARENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        popupWindow.showAtLocation(rl_main, gravity, 0, 0);
        MediaPlayer mediaPlayer2 = MediaPlayer.create(context, sounds[img]);

        final ImageView img_popup = (ImageView) popupView.findViewById(R.id.img_popup);
        img_popup.setImageResource(images[img]);
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.popup);
        img_popup.setAnimation(animation);

        stopMusic();

        if (WApp.getDataStorage().getSound()) {
            if (mediaPlayer2 != null) {
                mediaPlayer2.start();
            }
        }

        img_popup.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    img_popup.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    img_popup.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                new ParticleSystem((Activity) context, 50, R.drawable.star_yellow, 1000)
                        .setSpeedRange(0.1f, 0.15f)
                        .setScaleRange(0.7f, 1.0f)
                        .oneShot(img_popup, 40);
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mediaPlayer = MediaPlayer.create(context, R.raw.background);

                if (WApp.getDataStorage().getMusic()) {
                    playMusic();
                } else {
                    stopMusic();
                }

                popupWindow.dismiss();
            }
        }, 1000);
    }

}
