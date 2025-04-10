package jp.fluct.sample.kotlinapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import jp.fluct.fluctsdk.FluctAdRequestTargeting
import jp.fluct.fluctsdk.FluctErrorCode
import jp.fluct.fluctsdk.FluctRewardedVideo
import jp.fluct.fluctsdk.FluctRewardedVideoSettings
import java.util.*

class RewardedVideoActivity : AppCompatActivity() {

    companion object {

        private const val GROUP_ID = "1000090271"
        private const val UNIT_ID = "1000135434"

        private const val SAVED_STATE_UI_STATE_TEXT = "ui_state_text"
        private const val SAVED_STATE_UI_LOAD_BUTTON_ENABLED = "ui_load_button_enabled"
        private const val SAVED_STATE_UI_SHOW_BUTTON_ENABLED = "ui_show_button_enabled"

    }

    private lateinit var stateTextView: TextView
    private lateinit var showButton: Button
    private lateinit var loadButton: Button

    private fun getRewardedVideoInstance(): FluctRewardedVideo {
        val settings = FluctRewardedVideoSettings.Builder()
            .testMode(true)
            .debugMode(true)
            .build()
        return FluctRewardedVideo.getInstance(
            GROUP_ID,
            UNIT_ID,
            this,
            settings
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rewardedvideo)

        stateTextView = findViewById<TextView>(R.id.state_textview)
        showButton = findViewById<Button>(R.id.show_button)
        loadButton = findViewById<Button>(R.id.load_button)

        showButton.setOnClickListener {
            if (getRewardedVideoInstance().isAdLoaded) {
                getRewardedVideoInstance().show()
            }
        }
        loadButton.setOnClickListener {
            if (!getRewardedVideoInstance().isAdLoaded) {
                val targeting = FluctAdRequestTargeting()
                targeting.gender = FluctAdRequestTargeting.FluctGender.MALE
                targeting.birthday = GregorianCalendar(1988, Calendar.JANUARY, 1).time

                getRewardedVideoInstance().loadAd(targeting)

                updateUi(
                    state = "State: Loading",
                    loadButtonEnabled = false
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getRewardedVideoInstance().setListener(object : FluctRewardedVideo.Listener {
            // 広告読み込み完了
            override fun onLoaded(groupId: String?, unitId: String?) {
                updateUi(
                    state = "State: Loaded",
                    loadButtonEnabled = false,
                    showButtonEnabled = true
                )
                Log.d("RewardedVideoActivity", "onLoaded")
            }

            // 広告読み込み失敗
            override fun onFailedToLoad(groupId: String?, unitId: String?, errorCode: FluctErrorCode?) {
                updateUi(
                    state = "State: Failed to load",
                    loadButtonEnabled = true
                )
                Log.d("RewardedVideoActivity", "onFailedToLoad")
            }

            // 広告が表示した
            override fun onOpened(groupId: String?, unitId: String?) {
                Log.d("RewardedVideoActivity", "onOpened")
            }

            // 動画が再生された
            override fun onStarted(groupId: String?, unitId: String?) {
                Log.d("RewardedVideoActivity", "onStarted")
            }

            // リワード付与通知
            override fun onShouldReward(groupId: String?, unitId: String?) {
                Log.d("RewardedVideoActivity", "onShouldReward")
            }

            // 広告が閉じられた
            override fun onClosed(groupId: String?, unitId: String?) {
                updateUi(
                    state = "State: Complete",
                    loadButtonEnabled = true,
                    showButtonEnabled = false
                )
                Log.d("RewardedVideoActivity", "onClosed")
            }

            // 広告の再生が失敗した
            override fun onFailedToPlay(groupId: String?, unitId: String?, errorCode: FluctErrorCode?) {
                Log.d("RewardedVideoActivity", "onFailedToPlay")
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun updateUi(
        state: String,
        loadButtonEnabled: Boolean = loadButton.isEnabled,
        showButtonEnabled: Boolean = showButton.isEnabled
    ) {
        stateTextView.text = state
        loadButton.isEnabled = loadButtonEnabled
        showButton.isEnabled = showButtonEnabled
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SAVED_STATE_UI_STATE_TEXT, stateTextView.text.toString())
        outState.putBoolean(SAVED_STATE_UI_LOAD_BUTTON_ENABLED, loadButton.isEnabled)
        outState.putBoolean(SAVED_STATE_UI_SHOW_BUTTON_ENABLED, showButton.isEnabled)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        updateUi(
            state = savedInstanceState.getString(SAVED_STATE_UI_STATE_TEXT) ?: "",
            loadButtonEnabled = savedInstanceState.getBoolean(SAVED_STATE_UI_LOAD_BUTTON_ENABLED),
            showButtonEnabled = savedInstanceState.getBoolean(SAVED_STATE_UI_SHOW_BUTTON_ENABLED)
        )
    }
}
