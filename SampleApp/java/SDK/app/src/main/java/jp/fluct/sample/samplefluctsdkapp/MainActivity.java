package jp.fluct.sample.samplefluctsdkapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import jp.fluct.sample.samplefluctsdkapp.nativead.NativeAdSampleActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_banner).setOnClickListener(v ->
                startActivity(new Intent(this, BannerActivity.class)));

        findViewById(R.id.btn_rewardedvideo).setOnClickListener(v ->
                startActivity(new Intent(this, RewardedVideoActivity.class)));
        findViewById(R.id.btn_native).setOnClickListener(v ->
                startActivity(new Intent(this, NativeAdSampleActivity.class)));
    }
}
