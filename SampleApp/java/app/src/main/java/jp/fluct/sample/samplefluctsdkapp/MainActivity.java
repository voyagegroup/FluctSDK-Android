package jp.fluct.sample.samplefluctsdkapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_banner)
            .setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MainActivity.this, BannerActivity.class));
                    }
                }
            );

        findViewById(R.id.btn_rewardedvideo)
            .setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MainActivity.this, RewardedVideoActivity.class));
                    }
                }
            );

        findViewById(R.id.btn_videointerstitial)
            .setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(
                            new Intent(MainActivity.this, VideoInterstitialActivity.class)
                        );
                    }
                }
            );
    }
}
