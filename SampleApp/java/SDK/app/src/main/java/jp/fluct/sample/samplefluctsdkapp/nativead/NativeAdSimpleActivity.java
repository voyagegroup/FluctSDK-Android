package jp.fluct.sample.samplefluctsdkapp.nativead;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Calendar;
import java.util.GregorianCalendar;

import jp.fluct.fluctsdk.FluctAdError;
import jp.fluct.fluctsdk.FluctAdRequestTargeting;
import jp.fluct.fluctsdk.FluctNativeAd;
import jp.fluct.fluctsdk.FluctNativeAdContent;
import jp.fluct.fluctsdk.FluctViewBinder;
import jp.fluct.sample.samplefluctsdkapp.R;

public class NativeAdSimpleActivity extends Activity {
    private static final String TAG = NativeAdSimpleActivity.class.getSimpleName();

    private FluctNativeAd nativeAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_ad_simple);

        // 広告用のLayoutのidを設定する
        FluctViewBinder viewBinder = new FluctViewBinder.Builder(R.layout.native_ad_layout)
                .setMainMediaLayoutId(R.id.mainMedia)
                .setTitleId(R.id.title)
                .setDescriptionId(R.id.description)
                .setIconId(R.id.icon)
                .setCallToActionLabelId(R.id.callToAction)
                .setAdchoiceId(R.id.adchoice)
                .build();

        nativeAd = new FluctNativeAd(this, "1000098841", "1000149834", viewBinder);
        ViewGroup parent = findViewById(R.id.native_ad_simple);

        nativeAd.setListener(new FluctNativeAd.Listener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onLoaded(@NonNull FluctNativeAdContent fluctNativeAdContent) {
                // 広告の読み込みが完了した時に呼ばれる
                Log.i(TAG, "onLoaded");

                View adView = nativeAd.renderAdView(parent);
                if (adView == null) {
                    return;
                }

                parent.addView(adView);
                if (!fluctNativeAdContent.hasElement(FluctNativeAdContent.Element.CTA_LABEL)) {
                    // CTAボタンラベルがない場合はデフォルト値を設定する
                    Button cta = findViewById(R.id.callToAction);
                    cta.setText("Check Now!!");
                }
            }

            @Override
            public void onClicked(@NonNull FluctNativeAd fluctNativeAd) {
                // 広告がクリックされた時に呼ばれる
                Log.i(TAG, "onClicked");
            }

            @Override
            public void onLoggingImpression(@NonNull FluctNativeAd fluctNativeAd) {
                // 広告のImpressionが発生した時に呼ばれる
                Log.i(TAG, "onLoggingImpression");
            }

            @Override
            public void onFailedToLoad(@NonNull FluctAdError adError) {
                // 広告の読み込みが失敗した時に呼ばれる
                Log.i(TAG, "onFailedToLoad"
                        + "\n  ErrorCode: " + adError.getErrorCode()
                        + "\n  Message: " + adError.getErrorMessage());
            }

            @Override
            public void onFailedToRender(@NonNull FluctAdError adError) {
                // 広告の表示に失敗した時に呼ばれる
                Log.i(TAG, "onFailedToRender"
                        + "\n  ErrorCode: " + adError.getErrorCode()
                        + "\n  Message: " + adError.getErrorMessage());
            }
        });

        // ユーザ属性の設定を行うことで広告のターゲティングに利用することが出来ます
        FluctAdRequestTargeting targeting = new FluctAdRequestTargeting();
        targeting.setUserId("APP_USER_ID");
        targeting.setAge(18);
        targeting.setGender(FluctAdRequestTargeting.FluctGender.MALE);
        targeting.setBirthday(new GregorianCalendar(2000, Calendar.JANUARY, 1).getTime());

        // 広告読み込み
        nativeAd.loadAd(targeting);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        nativeAd.destroy();

        // ネイティブ広告で共通の画像リソースを解放を行う場合は下記をコールしてください
        // FluctNativeAd.destroyAll(this);
    }
}
