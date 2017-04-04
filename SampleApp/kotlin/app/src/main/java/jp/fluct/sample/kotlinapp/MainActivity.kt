package jp.fluct.sample.kotlinapp

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View


class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById(R.id.btn_banner).setOnClickListener(this)
        findViewById(R.id.btn_interstitial).setOnClickListener(this)
        findViewById(R.id.btn_infeed).setOnClickListener(this)
    }

    override fun onClick(v: View) {
        var intent: Intent? = null
        when (v.id) {
            R.id.btn_banner -> intent = Intent(this, BannerActivity::class.java)
            R.id.btn_interstitial -> intent = Intent(this, InterstitialActivity::class.java)
            R.id.btn_infeed -> intent = Intent(this, InfeedActivity::class.java)
        }

        if (intent != null) {
            startActivity(intent)
        }
    }
}