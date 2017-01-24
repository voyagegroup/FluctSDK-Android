package jp.fluct.sample.samplefluctsdkapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import jp.fluct.fluctsdk.FluctInterstitial;
import jp.fluct.fluctsdk.FluctView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_banner).setOnClickListener(this);
        findViewById(R.id.btn_interstitial).setOnClickListener(this);
        findViewById(R.id.btn_infeed).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.btn_banner:
                intent = new Intent(this, BannerActivity.class);
                break;
            case R.id.btn_interstitial:
                intent = new Intent(this, InterstitialActivity.class);
                break;
            case R.id.btn_infeed:
                intent = new Intent(this, InfeedActivity.class);
                break;
        }

        if (intent != null) {
            startActivity(intent);
        }
    }
}
