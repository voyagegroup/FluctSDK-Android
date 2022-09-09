package jp.fluct.sample.samplefluctsdkapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.GregorianCalendar;

import jp.fluct.fluctsdk.FluctAdRequestTargeting;
import jp.fluct.fluctsdk.FluctErrorCode;
import jp.fluct.fluctsdk.FluctVideoInterstitial;
import jp.fluct.fluctsdk.FluctVideoInterstitialSettings;

public class VideoInterstitialActivity extends AppCompatActivity {
    private FluctVideoInterstitial rewardedVideo;
    private Button showButton;
    private TextView stateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_interstitial);

        stateTextView = (TextView) findViewById(R.id.state_textview);

        String groupID = "1000120112";
        String unitID = "1000207345";

        FluctVideoInterstitialSettings settings = new FluctVideoInterstitialSettings.Builder()
                .testMode(true)
                .debugMode(true)
                .build();

        rewardedVideo = FluctVideoInterstitial.getInstance(groupID, unitID, this, settings);

        rewardedVideo.setListener(new FluctVideoInterstitial.Listener() {
            // 広告読み込み完了
            @Override
            public void onLoaded(String groupId, String unitId) {
                updateStateText("Loaded");
                showButton.setEnabled(true);
                Log.d("RewardedVideoActivity", "onLoaded");
            }

            // 広告読み込み失敗
            @Override
            public void onFailedToLoad(String groupId, String unitId, FluctErrorCode errorCode) {
                updateStateText("Failed to load");
                Log.d("RewardedVideoActivity", "onFailedToLoad");
            }

            // 広告が表示した
            @Override
            public void onOpened(String groupId, String unitId) {
                Log.d("RewardedVideoActivity", "onOpened");
            }

            // 動画が再生された
            @Override
            public void onStarted(String groupId, String unitId) {
                Log.d("RewardedVideoActivity", "onStarted");
            }

            // 広告が閉じられた
            @Override
            public void onClosed(String groupId, String unitId) {
                updateStateText("Complete");
                showButton.setEnabled(false);
                Log.d("RewardedVideoActivity", "onClosed");
            }

            // 広告の再生が失敗した
            @Override
            public void onFailedToPlay(String groupId, String unitId, FluctErrorCode errorCode) {
                updateStateText("Failed to play");
                Log.d("RewardedVideoActivity", "onFailedToPlay");
            }
        });

        showButton = (Button) findViewById(R.id.show_button);
        showButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!rewardedVideo.isAdLoaded()) {
                    return;
                }

                rewardedVideo.show();
            }
        });

        findViewById(R.id.load_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FluctAdRequestTargeting targeting = new FluctAdRequestTargeting();
                targeting.setGender(FluctAdRequestTargeting.FluctGender.MALE);
                targeting.setBirthday(new GregorianCalendar(1988, Calendar.JANUARY, 1).getTime());
                rewardedVideo.loadAd(targeting);
                updateStateText("Loading");
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void updateStateText(String s) {
        stateTextView.setText("State: " + s);
    }

}
