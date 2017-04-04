package jp.fluct.sample.kotlinapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import jp.fluct.fluctsdk.FluctAdBanner

class BannerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_banner)

        setupBanner()
    }

    private fun setupBanner() {
        val banner = findViewById(R.id.banner) as FluctAdBanner
        banner.setCallbacks(object : FluctAdBanner.Callbacks {
            override fun onDisplayDone() {
                // 広告が表示されたときの処理を記述（任意）

                // ...

            }

            override fun onTapped() {
                // 広告がタップされたときの処理を記述（任意）

                // ...

            }

            override fun onError(error: FluctAdBanner.Error) {
                when (error.type) {
                    FluctAdBanner.ErrorType.NetworkError -> {
                        // ネットワークエラー:通信状況を確認
                    }
                    FluctAdBanner.ErrorType.InvalidRequest -> {
                        // 不明な広告リクエスト:media_idを確認
                    }
                    else -> {
                        // その他の不明なエラー:エラーログを確認
                    }
                }
            }
        })
    }
}
