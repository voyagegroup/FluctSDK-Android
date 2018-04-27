package jp.fluct.sample.samplefluctsdkapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.GregorianCalendar;

import jp.fluct.fluctsdk.FluctAdRequestTargeting;
import jp.fluct.fluctsdk.FluctErrorCode;
import jp.fluct.fluctsdk.FluctRewardedVideo;
import jp.fluct.fluctsdk.FluctRewardedVideoSettings;

public class RewardedVideoActivity extends AppCompatActivity {
    private FluctRewardedVideo rewardedVideo;
    private Button showButton;
    private TextView stateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewarded_video);

        stateTextView = (TextView) findViewById(R.id.state_textview);

        String groupID = "1000090271";
        String unitID = "1000135434";


        FluctRewardedVideoSettings settings = new FluctRewardedVideoSettings.Builder()
                .testMode(true)
                .debugMode(true)
                .build();

        rewardedVideo = FluctRewardedVideo.getInstance(groupID, unitID, this, settings);

        rewardedVideo.setListener(new FluctRewardedVideo.Listener() {
            // 広告読み込み完了
            @Override
            public void onLoaded(String groupId, String unitId) {
                updateStateText("Loaded");
                showButton.setEnabled(true);
            }

            // 広告読み込み失敗
            @Override
            public void onFailedToLoad(String groupId, String unitId, FluctErrorCode errorCode) {
                updateStateText("Failed to load");
            }

            // 広告が表示した
            @Override
            public void onOpened(String groupId, String unitId) {

            }

            // 動画が再生された
            @Override
            public void onStarted(String groupId, String unitId) {

            }

            // リワード付与通知
            @Override
            public void onShouldReward(String groupId, String unitId) {

            }

            // 広告が閉じられた
            @Override
            public void onClosed(String groupId, String unitId) {
                updateStateText("Complete");
                showButton.setEnabled(false);
            }

            // 広告の再生が失敗した
            @Override
            public void onFailedToPlay(String groupId, String unitId, FluctErrorCode errorCode) {
                updateStateText("Failed to play");
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
                String userID = "APP_USER_ID";
                targeting.setUserId(userID);
                targeting.setGender(FluctAdRequestTargeting.FluctGender.MALE);
                targeting.setBirthday(new GregorianCalendar(1988, 1, 1).getTime());
                rewardedVideo.loadAd(targeting);
                updateStateText("Loading");
            }
        });
    }

    private void updateStateText(String s) {
        this.stateTextView.setText("State: " + s);
    }
}
