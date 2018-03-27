package jp.fluct.sample.kotlinapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import jp.fluct.fluctsdk.FluctAdRequestTargeting
import jp.fluct.fluctsdk.FluctErrorCode
import jp.fluct.fluctsdk.FluctRewardedVideo
import jp.fluct.fluctsdk.FluctRewardedVideoSettings

/**
 * Created by t-sei on 2018/02/14.
 */
class RewardedVideoActivity : AppCompatActivity() {
    private var rewardedVideo: FluctRewardedVideo? = null
    private var stateTextView: TextView? = null
    private var showButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rewardedvideo)

        stateTextView = findViewById<TextView>(R.id.state_textview)

        val groupID = "1000090271"
        val unitID = "1000135434"

        val settings = FluctRewardedVideoSettings.Builder()
                .testMode(true)
                .debugMode(true)
                .build()

        rewardedVideo = FluctRewardedVideo.getInstance(groupID, unitID, this, settings)
        rewardedVideo?.setListener(object : FluctRewardedVideo.Listener {
            // 広告読み込み完了
            override fun onLoaded(groupId: String?, unitId: String?) {
                showButton?.isEnabled = true
                updateStateTextView("Loaded")
            }

            // 広告読み込み失敗
            override fun onFailedToLoad(groupId: String?, unitId: String?, errorCode: FluctErrorCode?) {
                updateStateTextView("Failed to load")
            }

            // 広告が表示した
            override fun onOpened(groupId: String?, unitId: String?) {
            }

            // 動画が再生された
            override fun onStarted(groupId: String?, unitId: String?) {
            }

            // リワード付与通知
            override fun onShouldReward(groupId: String?, unitId: String?) {
            }

            // 広告が閉じられた
            override fun onClosed(groupId: String?, unitId: String?) {
                showButton?.isEnabled = false
                updateStateTextView("Complete")
            }

            // 広告の再生が失敗した
            override fun onFailedToPlay(groupId: String?, unitId: String?, errorCode: FluctErrorCode?) {
            }
        })

        showButton = findViewById<Button>(R.id.show_button)
        showButton?.setOnClickListener {
            if (rewardedVideo?.isAdLoaded == true) {
                rewardedVideo?.show()
            }
        }
        findViewById<Button>(R.id.load_button).setOnClickListener {
            val targeting = FluctAdRequestTargeting()
            val userID = "APP_USER_ID"
            targeting.setUserId(userID)
            rewardedVideo?.loadAd(targeting)
            updateStateTextView("Loading")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    fun updateStateTextView(text: String) {
        stateTextView?.setText("State: " + text)
    }
}
