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

public class ExpertActivity extends AppCompatActivity implements View.OnClickListener {


    private MediaPlayer mediaPlayer1;
    private RecyclerView list_expert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expert);

        mediaPlayer1 = MediaPlayer.create(this, R.raw.button_click);
        findViewById(R.id.img_back).setOnClickListener(this);

        list_expert = (RecyclerView) findViewById(R.id.list_expert);
        list_expert.setLayoutManager(new GridLayoutManager(ExpertActivity.this, 3));

        ExpertAdapter expertAdapter = new ExpertAdapter();
        list_expert.setAdapter(expertAdapter);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ExpertActivity.this, AchieveActivity.class);
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
                Intent intent = new Intent(ExpertActivity.this, AchieveActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    public class ExpertAdapter extends RecyclerView.Adapter<ExpertAdapter.ExpertViewHolder> {

        public ExpertAdapter() {
        }
       /* selected
        @NonNull
        @Override*/
        public ExpertAdapter.ExpertViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(ExpertActivity.this).inflate(R.layout.item_basic, viewGroup, false);

            ExpertAdapter.ExpertViewHolder expertViewHolder = new ExpertAdapter.ExpertViewHolder(v);

            return expertViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ExpertAdapter.ExpertViewHolder expertViewHolder, final int i) {

//            final int j=i+100;
            final int j=i+100;
            int level = WApp.getDataStorage().getSetting_Level();

            if (j < level) {
                expertViewHolder.lock_expert.setVisibility(View.GONE);
                expertViewHolder.level_expert.setText((i+1) + "");
            } else if (j == level) {
                expertViewHolder.lock_expert.setVisibility(View.GONE);
                expertViewHolder.level_expert.setText((i +1) + "");
                expertViewHolder.level_expert.setTextColor(getResources().getColor(R.color.orange));
            } else {
                expertViewHolder.level_expert.setVisibility(View.GONE);
                expertViewHolder.lock_expert.setVisibility(View.VISIBLE);
            }

            if (j <= level) {
                expertViewHolder.expert.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        WApp.getDataStorage().setLevel(j);
                        WApp.getDataStorage().setLevelCount(i);
                        Intent intent = new Intent(ExpertActivity.this, GameActivity.class);
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

        public class ExpertViewHolder extends RecyclerView.ViewHolder {

            TextView level_expert;
            ImageView lock_expert, expert;

            public ExpertViewHolder(@NonNull View itemView) {
                super(itemView);
                expert = (ImageView) itemView.findViewById(R.id.basic);
                lock_expert = (ImageView) itemView.findViewById(R.id.lock_basic);
                level_expert = (TextView) itemView.findViewById(R.id.level_basic);
            }
        }
    }

}
