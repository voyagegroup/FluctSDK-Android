package jp.fluct.sample.kotlinapp.banner

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import jp.fluct.fluctsdk.FluctAdView
import jp.fluct.sample.kotlinapp.R

class BannerLayoutXmlActivity : AppCompatActivity() {

    private val adView by lazy { findViewById<FluctAdView>(R.id.adView) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.banner_layout_xml_activity)

        adView.loadAd()
    }

}
