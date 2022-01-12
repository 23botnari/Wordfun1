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

public class InterActivity extends AppCompatActivity implements View.OnClickListener {

    private MediaPlayer mediaPlayer1;
    private RecyclerView list_inter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inter);

        mediaPlayer1 = MediaPlayer.create(this, R.raw.button_click);
        findViewById(R.id.img_back).setOnClickListener(this);

        list_inter = (RecyclerView) findViewById(R.id.list_inter);
        list_inter.setLayoutManager(new GridLayoutManager(InterActivity.this, 3));

        InterAdapter interAdapter = new InterAdapter();
        list_inter.setAdapter(interAdapter);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(InterActivity.this, AchieveActivity.class);
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
                Intent intent = new Intent(InterActivity.this, AchieveActivity.class);
                startActivity(intent);
                finish();
                break;

        }
    }

    public class InterAdapter extends RecyclerView.Adapter<InterAdapter.InterViewHolder> {

        public InterAdapter() {
        }

        @NonNull
        @Override
        public InterAdapter.InterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(InterActivity.this).inflate(R.layout.item_basic, viewGroup, false);

            InterAdapter.InterViewHolder interViewHolder = new InterAdapter.InterViewHolder(v);

            return interViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull InterAdapter.InterViewHolder interViewHolder, final int i) {
//            final int j=i+50;
            final int j=i+50;
            int level = WApp.getDataStorage().getSetting_Level();

                if (j < level) {
                    interViewHolder.lock_inter.setVisibility(View.GONE);
                    interViewHolder.level_inter.setText((i+1) + "");
                } else if (j == level) {
                    interViewHolder.lock_inter.setVisibility(View.GONE);
                    interViewHolder.level_inter.setText((i +1) + "");
                    interViewHolder.level_inter.setTextColor(getResources().getColor(R.color.orange));
                } else {
                    interViewHolder.level_inter.setVisibility(View.GONE);
                    interViewHolder.lock_inter.setVisibility(View.VISIBLE);
                }

                if (j <= level) {
                    interViewHolder.inter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            WApp.getDataStorage().setLevel(j);
                            WApp.getDataStorage().setLevelCount(i);
                            Intent intent = new Intent(InterActivity.this, GameActivity.class);
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

        public class InterViewHolder extends RecyclerView.ViewHolder {

            TextView level_inter;
            ImageView lock_inter, inter;

            public InterViewHolder(@NonNull View itemView) {
                super(itemView);
                inter = (ImageView) itemView.findViewById(R.id.basic);
                lock_inter = (ImageView) itemView.findViewById(R.id.lock_basic);
                level_inter = (TextView) itemView.findViewById(R.id.level_basic);
            }
        }
    }

}
