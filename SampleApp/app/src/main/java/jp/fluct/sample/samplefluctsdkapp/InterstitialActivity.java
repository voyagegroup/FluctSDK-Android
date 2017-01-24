package jp.fluct.sample.samplefluctsdkapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import jp.fluct.fluctsdk.FluctInterstitial;

public class InterstitialActivity extends AppCompatActivity {

    private FluctInterstitial mInterstitial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interstitial);

        setupInterstitial();
    }

    @Override
    protected void onDestroy() {

        destroyInterstitial();

        super.onDestroy();
    }

    private void setupInterstitial() {
        mInterstitial = new FluctInterstitial(this, "0000000108");
        mInterstitial.setCallback(new FluctInterstitial.Callback() {
            @Override
            public void onDisplayDone(boolean b) {
                if (b) {
                    // 広告が表示されたときの処理を記述（任意）

                    // ...

                } else {
                    // 表示できる広告がなかったときの処理を記述（任意）

                    // ...

                }
            }

            @Override
            public void onTap() {
                // 広告がタップされたときの処理を記述（任意）

                // ...

            }

            @Override
            public void onClose() {
                // 広告が閉じられたときの処理を記述（任意）

                // ...

            }

            @Override
            public void onError(FluctInterstitial.FluctInterstitialError fluctInterstitialError) {
                switch (fluctInterstitialError.getType()){
                    case NetworkError:
                        // ネットワークエラー:通信状況を確認
                        break;
                    case InvalidRequest:
                        // 不明な広告リクエスト:media_idを確認
                        break;
                    case InternalError:
                    default:
                        // その他の不明なエラー:エラーログを確認
                        break;
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