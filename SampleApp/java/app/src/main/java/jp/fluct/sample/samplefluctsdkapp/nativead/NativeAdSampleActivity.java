package jp.fluct.sample.samplefluctsdkapp.nativead;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import jp.fluct.sample.samplefluctsdkapp.R;

public class NativeAdSampleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_ad_sample);

        findViewById(R.id.btn_simple).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(
                        NativeAdSampleActivity.this,
                        NativeAdSimpleActivity.class
                ));
            }

        });

        findViewById(R.id.btn_recycler).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(
                        NativeAdSampleActivity.this,
                        NativeAdRecyclerActivity.class
                ));
            }

        });
    }
}
