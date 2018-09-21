package jp.fluct.sample.kotlinapp.nativead

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import jp.fluct.fluctsdk.*
import jp.fluct.sample.kotlinapp.R
import java.util.*

class NativeAdRecyclerActivity : AppCompatActivity() {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: NativeAdItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_native_ad_recycler)

        mRecyclerView = findViewById(R.id.recycler_view)

        // ユーザ属性の設定を行うことで広告のターゲティングに利用することが出来ます
        val targeting = FluctAdRequestTargeting().apply {
            setUserId("f8c5cbe3-9da9-4ec5-a1a2-948c79eb8391")
            age = 18
            gender = FluctAdRequestTargeting.FluctGender.MALE
            birthday = GregorianCalendar(2000, Calendar.JANUARY, 1).time
        }

        mAdapter = NativeAdItemAdapter(this, targeting).apply {
            setItems(createSampleData())
        }
        mRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@NativeAdRecyclerActivity)
            adapter = mAdapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mAdapter.destroy()
    }

    private fun createSampleData(): List<String> {
        return List(100) { "Content Item #${it + 1}" }
    }

    private class NativeAdItemAdapter internal constructor(
            private val mContext: Context,
            private val mAdRequestTargeting: FluctAdRequestTargeting?
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        companion object {
            private const val GROUP_ID = "1000098841"
            private const val UNIT_ID = "1000149834"

            private const val AD_POSITION_INTERVAL = 30
        }

        enum class ViewType(
                val id: Int
        ) {
            ITEM(0),
            AD(1),
            NO_AD(2);

            companion object {

                fun fromId(id: Int): ViewType? {
                    return values().find { it.id == id }
                }

            }

        }

        /**
         * 広告を表示するpositionとそれに紐付けられた`FluctNativeAd`インスタンスを保持する
         */
        private val mNativeAds = mutableMapOf<Int, FluctNativeAd>()

        private var mItems = emptyList<String>()

        internal fun setItems(items: List<String>) {
            mItems = items
            setupAds()
            notifyDataSetChanged()
        }

        internal fun destroy() {
            // `destroy`メソッドを呼ぶことで、任意のタイミングでリソースの解放を行うことが出来ます
            mNativeAds.values.forEach { it.destroy() }
        }

        override fun getItemViewType(position: Int): Int {
            return mNativeAds[position].let {
                if (it == null) {
                    ViewType.ITEM
                } else {
                    if (it.isLoaded) ViewType.AD else ViewType.NO_AD
                }
            }.id
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return when (ViewType.fromId(viewType)!!) {
                ViewType.ITEM ->
                    ItemViewHolder(TextView(parent.context).apply {
                        layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                TypedValue.applyDimension(
                                        TypedValue.COMPLEX_UNIT_DIP,
                                        60f,
                                        parent.resources.displayMetrics
                                ).toInt()
                        )
                    })
                ViewType.AD ->
                    NativeAdViewHolder(LinearLayout(parent.context).apply {
                        layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        )
                    })
                ViewType.NO_AD -> // 広告の読込が完了していない場合、一時的に空のViewを表示します
                    NoAdViewHolder(View(parent.context).apply {
                        layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        )
                    })
            }
        }

        override fun getItemCount(): Int {
            // 表示する広告分countを増加させる
            return mItems.size + mNativeAds.values.filter { it.isLoaded }.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            when (ViewType.fromId(getItemViewType(position))!!) {
                ViewType.ITEM ->
                    (holder as ItemViewHolder).setText(getItem(position))
                ViewType.AD -> run {
                    val nativeAd = mNativeAds[position]!!
                    // 広告表示時は毎回renderAdViewをコールしてください
                    (holder as NativeAdViewHolder).apply {
                        nativeAd.renderAdView(mLayout)?.let { adView ->
                            nativeAd.adContent?.let {
                                if (!it.hasElement(FluctNativeAdContent.Element.CTA_LABEL))
                                    adView.findViewById<View>(R.id.callToAction).visibility = View.GONE
                                if (!it.hasElement(FluctNativeAdContent.Element.DESCRIPTION))
                                    adView.findViewById<View>(R.id.description).visibility = View.GONE
                            }
                            setAdView(adView)
                        }
                    }
                }
                ViewType.NO_AD -> Unit // no-op
            }
        }

        private fun getItem(position: Int): String? {
            return when (ViewType.fromId(getItemViewType(position))) {
                ViewType.ITEM -> run {
                    // 広告の分positionをずらす
                    val itemPosition = position - mNativeAds.filter { it.key < position }.values.size
                    mItems[itemPosition]
                }
                else ->
                    throw IllegalStateException()
            }
        }

        private fun setupAds() {
            // 広告用のLayoutのidを設定する
            val viewBinder = FluctViewBinder.Builder(R.layout.native_ad_layout)
                    .setMainMediaLayoutId(R.id.mainMedia)
                    .setTitleId(R.id.title)
                    .setDescriptionId(R.id.description)
                    .setIconId(R.id.icon)
                    .setCallToActionLabelId(R.id.callToAction)
                    .setAdchoiceId(R.id.adchoice)
                    .build()

            val adCount = mItems.size / AD_POSITION_INTERVAL
            for (i in 1..adCount) {
                val position = i * (AD_POSITION_INTERVAL + 1) - 1 // 30個間隔に配置（30, 61, 92...）
                if (mNativeAds[position] != null || mItems.size <= position) {
                    continue
                }
                val nativeAd = FluctNativeAd(mContext, GROUP_ID, UNIT_ID, viewBinder)
                nativeAd.setListener(FluctNativeAdListener(position))
                mNativeAds[position] = nativeAd
                nativeAd.loadAd(mAdRequestTargeting)
            }
        }

        internal inner class FluctNativeAdListener(
                private val position: Int
        ) : FluctNativeAd.Listener {

            override fun onLoaded(content: FluctNativeAdContent) {
                // 広告の読み込みが完了した時に呼ばれる
                Log.i("NativeAdSample", "onLoaded")
                notifyItemChanged(position)
            }

            override fun onClicked(fluctNativeAd: FluctNativeAd) {
                // 広告がクリックされた時に呼ばれる
                Log.i("NativeAdSample", "onClicked")
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
                notifyItemChanged(position)
            }
        }
    }

    private class ItemViewHolder internal constructor(
            private val mTextView: TextView
    ) : RecyclerView.ViewHolder(mTextView) {

        internal fun setText(text: String?) {
            mTextView.text = text
        }
    }

    private class NativeAdViewHolder internal constructor(
            internal val mLayout: LinearLayout
    ) : RecyclerView.ViewHolder(mLayout) {

        internal fun setAdView(adView: View) {
            mLayout.removeAllViews()
            mLayout.addView(
                    adView,
                    ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    )
            )
        }
    }

    private class NoAdViewHolder(
            root: View
    ) : RecyclerView.ViewHolder(root)

}

