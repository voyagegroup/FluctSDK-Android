package jp.fluct.sample.samplefluctsdkapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import jp.fluct.fluctsdk.FluctAdBanner;

public class BannerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);

        setupBanner();
    }

    private void setupBanner() {
        FluctAdBanner banner = (FluctAdBanner) findViewById(R.id.banner);
        banner.setCallbacks(new FluctAdBanner.Callbacks() {
            @Override
            public void onDisplayDone() {
                // 広告が表示されたときの処理を記述（任意）

                // ...

            }

            @Override
            public void onTapped() {
                // 広告がタップされたときの処理を記述（任意）

                // ...

            }

            @Override
            public void onError(FluctAdBanner.Error error) {
                switch (error.getType()){
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
    }
}