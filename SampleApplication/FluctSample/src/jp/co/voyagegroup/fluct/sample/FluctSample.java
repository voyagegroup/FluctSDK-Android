package jp.co.voyagegroup.fluct.sample;

import jp.co.voyagegroup.android.fluct.jar.FluctInterstitial;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class FluctSample extends Activity {

    private FluctInterstitial mFluctInterstitial;
    private final FluctInterstitialCallback mInterstitialCallback = new FluctInterstitialCallback();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fluct_sample);
        mFluctInterstitial = new FluctInterstitial(getApplicationContext());
        mFluctInterstitial.setFluctInterstitialCallback(mInterstitialCallback);
        Button showIntersitial = (Button)findViewById(R.id.showIntersitial);
        showIntersitial.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view) {
                mFluctInterstitial.showIntersitialAd();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_fluct_sample, menu);
        return true;
    }

    private final class FluctInterstitialCallback implements jp.co.voyagegroup.android.fluct.jar.FluctInterstitial.FluctInterstitialCallback {
        public void onReceiveAdInfo(int status) {
        }
    }
}
