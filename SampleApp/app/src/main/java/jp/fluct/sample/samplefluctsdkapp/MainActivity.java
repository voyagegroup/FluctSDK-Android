package jp.fluct.sample.samplefluctsdkapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import jp.fluct.fluctsdk.FluctInterstitial;
import jp.fluct.fluctsdk.FluctView;

public class MainActivity extends AppCompatActivity {

    private FluctInterstitial mInterstitial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        setupBanner();

        setupInterstitial();
    }

    @Override
    protected void onDestroy() {

        destroyInterstitial();

        super.onDestroy();
    }

    private void setupBanner() {
        FluctView banner = (FluctView) findViewById(R.id.banner);
        banner.setCallback(new FluctView.Callback() {
            @Override
            public void onDisplayDone(boolean b) {
                if (!b) {
                    // TODO: Not displayed Ad codes
                } else {
                    // TODO: Displayed Ad codes
                }
            }

            @Override
            public void onTap() {
                // TODO: Tapped codes
            }

            @Override
            public void onError(FluctView.FluctViewError fluctViewError) {
                if (FluctView.ErrorType.InternalError == fluctViewError.getType()) {
                    // TODO: Internal error codes
                } else if (FluctView.ErrorType.InvalidRequest == fluctViewError.getType()) {
                    // TODO: Invalid request codes
                } else if (FluctView.ErrorType.NetworkError == fluctViewError.getType()) {
                    // TODO: Network error codes
                }
            }
        });
    }

    private void setupInterstitial() {
        mInterstitial = new FluctInterstitial(this, "0000000108");
        mInterstitial.setCallback(new FluctInterstitial.Callback() {
            @Override
            public void onDisplayDone(boolean b) {
                if (!b) {
                    // TODO: Not displayed Ad codes
                } else {
                    // TODO: Displayed Ad codes
                }
            }

            @Override
            public void onTap() {
                // TODO: Tapped codes
            }

            @Override
            public void onClose() {
                // TODO: Closed codes
            }

            @Override
            public void onError(FluctInterstitial.FluctInterstitialError fluctInterstitialError) {
                if (FluctInterstitial.ErrorType.InternalError == fluctInterstitialError.getType()) {
                    // TODO: Internal error codes
                } else if (FluctInterstitial.ErrorType.InvalidRequest == fluctInterstitialError.getType()) {
                    // TODO: Invalid request codes
                } else if (FluctInterstitial.ErrorType.NetworkError == fluctInterstitialError.getType()) {
                    // TODO: Network error codes
                }
            }
        });

        Button button = (Button) findViewById(R.id.interstitial);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mInterstitial.showInterstitialAd();
            }
        });
    }

    private void destroyInterstitial() {
        mInterstitial.destroy();
        mInterstitial = null;
    }
}
