package jp.fluct.sample.samplefluctsdkapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import jp.fluct.sample.samplefluctsdkapp.banner.BannerLayoutXmlActivity;
import jp.fluct.sample.samplefluctsdkapp.banner.BannerRecyclerActivity;

public class BannerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.banner_activity);

        this.<Button>findViewById(R.id.impl_on_xml).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(
                        new Intent(BannerActivity.this, BannerLayoutXmlActivity.class)
                );
            }

        });

        this.<Button>findViewById(R.id.recycler).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(
                        new Intent(BannerActivity.this, BannerRecyclerActivity.class)
                );
            }

        });
    }
}
