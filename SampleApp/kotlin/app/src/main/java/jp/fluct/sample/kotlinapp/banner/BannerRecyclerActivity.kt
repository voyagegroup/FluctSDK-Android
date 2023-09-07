package jp.fluct.sample.kotlinapp.banner

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import jp.fluct.fluctsdk.FluctAdView
import jp.fluct.fluctsdk.FluctErrorCode
import jp.fluct.sample.kotlinapp.R

class BannerRecyclerActivity : AppCompatActivity() {

    private val recycler by lazy { findViewById<RecyclerView>(R.id.recycler) }
    private lateinit var adView: FluctAdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.banner_recycler_activity)

        recycler.adapter = MyAdapter()
        recycler.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )

        adView = FluctAdView(
            this,
            getString(R.string.banner_test_group_id),
            getString(R.string.banner_test_unit_id),
            getString(R.string.banner_test_ad_size),
            null,
            object : FluctAdView.Listener {

                override fun onUnloaded() {
                    toast("onUnloaded")
                }

                override fun onLeftApplication() {
                    toast("onLeftApplication")
                }

                override fun onLoaded() {
                    toast("onLoaded")
                }

                override fun onFailedToLoad(p0: FluctErrorCode) {
                    toast("onFailedToLoad")
                }

            }
        )

        adView.loadAd()
    }

    private fun toast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    private inner class MyAdapter : RecyclerView.Adapter<MyVH>() {

        override fun getItemViewType(position: Int): Int {
            return if ((position + 1) % 50 == 0) 0 else 1
        }

        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MyVH {
            return when (viewType) {
                0 -> MyAdVH(viewGroup)
                1 -> MyTextVH(viewGroup)
                else -> throw IllegalStateException()
            }
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(vh: MyVH, position: Int) {
            when (vh) {
                is MyAdVH -> run {
                    if (vh.container.childCount > 0) {
                        vh.container.removeAllViews()
                    }

                    vh.container.addView(adView)
                }
                is MyTextVH -> vh.text.text = "Position: $position"
            }
        }

        override fun getItemCount(): Int {
            return 1000
        }

    }

}

private sealed class MyVH(itemView: View) : RecyclerView.ViewHolder(itemView)

private class MyAdVH(parent: ViewGroup) : MyVH(
    LayoutInflater.from(parent.context)
        .inflate(R.layout.banner_recycler_ad_item_layout, parent, false)
) {

    val container by lazy { itemView.findViewById<ViewGroup>(R.id.container) }

}

private class MyTextVH(parent: ViewGroup) : MyVH(
    LayoutInflater.from(parent.context)
        .inflate(R.layout.banner_recycler_text_item_layout, parent, false)
) {

    val text by lazy { itemView.findViewById<TextView>(R.id.text) }

}
