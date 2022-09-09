package jp.fluct.sample.kotlinapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import jp.fluct.sample.kotlinapp.banner.BannerLayoutXmlActivity
import jp.fluct.sample.kotlinapp.banner.BannerRecyclerActivity

class BannerActivity : AppCompatActivity() {

    private val implOnXml by lazy { findViewById<Button>(R.id.impl_on_xml) }
    private val implOnRecycler by lazy { findViewById<Button>(R.id.impl_on_recycler) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.banner_activity)

        implOnXml.setOnClickListener {
            startActivity(Intent(this, BannerLayoutXmlActivity::class.java))
        }

        implOnRecycler.setOnClickListener {
            startActivity(Intent(this, BannerRecyclerActivity::class.java))
        }
    }

}
