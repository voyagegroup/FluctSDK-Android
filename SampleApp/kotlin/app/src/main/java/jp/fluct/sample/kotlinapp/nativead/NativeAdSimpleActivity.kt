package jp.fluct.sample.kotlinapp.nativead

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.RelativeLayout
import jp.fluct.fluctsdk.*
import jp.fluct.sample.kotlinapp.R
import java.util.*

class NativeAdSimpleActivity : AppCompatActivity() {
    private val TAG = NativeAdSimpleActivity::class.java.simpleName
    private lateinit var nativeAd: FluctNativeAd

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_native_ad_simple)

        // 広告用のLayoutのidを設定する
        val viewBinder = FluctViewBinder.Builder(R.layout.native_ad_layout)
                .setMainMediaLayoutId(R.id.mainMedia)
                .setTitleId(R.id.title)
                .setDescriptionId(R.id.description)
                .setIconId(R.id.icon)
                .setCallToActionLabelId(R.id.callToAction)
                .setAdchoiceId(R.id.adchoice)
                .build()

        nativeAd = FluctNativeAd(this, "1000098841", "1000149834", viewBinder)
        val parent = findViewById<RelativeLayout>(R.id.native_ad_simple)

        nativeAd.setListener(object : FluctNativeAd.Listener {

            @SuppressLint("SetTextI18n")
            override fun onLoaded(nativeAdContent: FluctNativeAdContent) {
                // 広告の読み込みが完了した時に呼ばれる
                Log.i(TAG, "onLoaded")
                val adView = nativeAd.renderAdView(parent)?.also {
                    parent.addView(it)
                } ?: return

                // CTAボタンラベルがない場合はデフォルト値を設定する
                if (!nativeAdContent.hasElement(FluctNativeAdContent.Element.CTA_LABEL))
                    adView.findViewById<Button>(R.id.callToAction).text = "Check Now!!"
            }

            override fun onClicked(fluctNativeAd: FluctNativeAd) {
                // 広告がクリックされた時に呼ばれる
                Log.i(TAG, "onClicked")
            }

            override fun onLoggingImpression(fluctNativeAd: FluctNativeAd) {
                // 広告のImpressionが発生した時に呼ばれる
                Log.i("NativeAdSample", "onLoggingImpression")
            }

            override fun onFailedToLoad(adError: FluctAdError) {
                // 広告の読み込みが失敗した時に呼ばれる
                Log.i("NativeAdSample", "onFailedToLoad"
                        + "\n  ErrorCode: " + adError.errorCode
                        + "\n  Message: " + adError.errorMessage)
            }

            override fun onFailedToRender(adError: FluctAdError) {
                // 広告の表示に失敗した時に呼ばれる
                Log.i("NativeAdSample", "onFailedToRender"
                        + "\n  ErrorCode: " + adError.errorCode
                        + "\n  Message: " + adError.errorMessage)
            }
        })


        // ユーザ属性の設定を行うことで広告のターゲティングに利用することが出来ます
        val targeting = FluctAdRequestTargeting().apply {
            setUserId("f8c5cbe3-9da9-4ec5-a1a2-948c79eb8391")
            age = 18
            gender = FluctAdRequestTargeting.FluctGender.MALE
            birthday = GregorianCalendar(2000, Calendar.JANUARY, 1).time
        }

        // 広告読み込み
        nativeAd.loadAd(targeting)
    }

    override fun onDestroy() {
        super.onDestroy()
        nativeAd.destroy()


        // ネイティブ広告で共通の画像リソースを解放を行う場合は下記をコールしてください
        // FluctNativeAd.destroyAll(this)
    }
}
