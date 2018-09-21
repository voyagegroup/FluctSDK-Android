package jp.fluct.sample.kotlinapp.nativead

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import jp.fluct.sample.kotlinapp.R

class NativeAdSampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_native_ad_sample)

        findViewById<Button>(R.id.btn_simple).setOnClickListener {
            startActivity(Intent(this, NativeAdSimpleActivity::class.java))
        }
        findViewById<Button>(R.id.btn_recycler).setOnClickListener {
            startActivity(Intent(this, NativeAdRecyclerActivity::class.java))
        }
    }
}
