package jp.fluct.sample.kotlinapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.Toast
import jp.fluct.fluctsdk.FluctInterstitial

class InterstitialActivity : AppCompatActivity() {

    private var interstitial: FluctInterstitial? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interstitial)

        setupInterstitial()
    }

    override fun onDestroy() {
        destroyInterstitial()
        super.onDestroy()
    }

    private fun setupInterstitial() {
        interstitial = FluctInterstitial(this, "0000000108")
        interstitial!!.setCallback(object : FluctInterstitial.Callback {
            override fun onDisplayDone(b: Boolean) {
                if (b) {
                    // 広告が表示されたときの処理を記述（任意）

                    // ...

                } else {
                    // 表示できる広告がなかったときの処理を記述（任意）

                    // ...

                }
            }

            override fun onTap() {
                // 広告がタップされたときの処理を記述（任意）

                // ...

            }

            override fun onClose() {
                // 広告が閉じられたときの処理を記述（任意）

                // ...

            }

            override fun onError(fluctInterstitialError: FluctInterstitial.FluctInterstitialError) {
                when (fluctInterstitialError.type) {
                    FluctInterstitial.ErrorType.NetworkError -> {
                        // ネットワークエラー:通信状況を確認
                    }
                    FluctInterstitial.ErrorType.InvalidRequest -> {
                        // 不明な広告リクエスト:media_idを確認
                    }
                    else -> {
                        // その他の不明なエラー:エラーログを確認
                    }
                }
            }
        })

        val button = findViewById<Button>(R.id.interstitial)
        button.setOnClickListener {
            interstitial?.showInterstitialAd()
        }
    }

    private fun destroyInterstitial() {
        interstitial?.destroy()
        interstitial = null
    }
}
