package jp.fluct.sample.kotlinapp

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import jp.fluct.sample.kotlinapp.nativead.NativeAdSampleActivity


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btn_banner).setOnClickListener {
            startActivity(Intent(this, BannerActivity::class.java))
        }

        findViewById<Button>(R.id.btn_rewardedvideo).setOnClickListener {
            startActivity(Intent(this, RewardedVideoActivity::class.java))
        }

        findViewById<Button>(R.id.btn_videointerstitial).setOnClickListener {
            startActivity(Intent(this, VideoInterstitialActivity::class.java))
        }

        findViewById<Button>(R.id.btn_native).setOnClickListener {
            startActivity(Intent(this, NativeAdSampleActivity::class.java))
        }
    }
}
