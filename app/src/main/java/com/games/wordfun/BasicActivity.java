package com.games.wordfun;

import android.content.Intent;
import android.media.MediaPlayer;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class BasicActivity extends AppCompatActivity implements View.OnClickListener {

    private MediaPlayer mediaPlayer1;
    private RecyclerView list_basic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);
        mediaPlayer1 = MediaPlayer.create(this, R.raw.button_click);
        findViewById(R.id.img_back).setOnClickListener(this);

        list_basic = (RecyclerView) findViewById(R.id.list_basic);
        list_basic.setLayoutManager(new GridLayoutManager(BasicActivity.this, 3));

        BasicAdapter basicAdapter = new BasicAdapter();
        list_basic.setAdapter(basicAdapter);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(BasicActivity.this, AchieveActivity.class);
        startActivity(intent);
        finish();
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
                Intent intent = new Intent(BasicActivity.this, AchieveActivity.class);
                startActivity(intent);
                finish();
                break;

        }
    }

    public class BasicAdapter extends RecyclerView.Adapter<BasicAdapter.BasicViewHolder> {

        public BasicAdapter() {
        }

        @NonNull
        @Override
        public BasicViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(BasicActivity.this).inflate(R.layout.item_basic, viewGroup, false);

            BasicViewHolder basicViewHolder = new BasicViewHolder(v);

            return basicViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull BasicViewHolder basicViewHolder, final int i) {

            int level = WApp.getDataStorage().getSetting_Level();

            if (i < level) {
                basicViewHolder.lock_basic.setVisibility(View.GONE);
                basicViewHolder.level_basic.setText((i + 1) + "");
            } else if (i == level) {
                basicViewHolder.lock_basic.setVisibility(View.GONE);
                basicViewHolder.level_basic.setText((i + 1) + "");
                basicViewHolder.level_basic.setTextColor(getResources().getColor(R.color.orange));
            } else {
                basicViewHolder.level_basic.setVisibility(View.GONE);
                basicViewHolder.lock_basic.setVisibility(View.VISIBLE);
            }

            if (i<=level){
                basicViewHolder.basic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        WApp.getDataStorage().setLevel(i);
                        WApp.getDataStorage().setLevelCount(i);
                        Intent intent = new Intent(BasicActivity.this, GameActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return 50;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        public class BasicViewHolder extends RecyclerView.ViewHolder {

            TextView level_basic;
            ImageView lock_basic,basic;

            public BasicViewHolder(@NonNull View itemView) {
                super(itemView);
                basic = (ImageView) itemView.findViewById(R.id.basic);
                lock_basic = (ImageView) itemView.findViewById(R.id.lock_basic);
                level_basic = (TextView) itemView.findViewById(R.id.level_basic);
            }
        }
    }
}
