package jp.fluct.sample.samplefluctsdkapp.banner;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import jp.fluct.fluctsdk.FluctAdView;
import jp.fluct.sample.samplefluctsdkapp.R;

public class BannerLayoutXmlActivity extends AppCompatActivity {

    private FluctAdView banner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.banner_layout_xml_activity);

        banner = findViewById(R.id.banner);

        banner.loadAd();
    }

}
