package jp.fluct.sample.kotlinapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class InfeedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_infeed)

        if (savedInstanceState == null) {
            val fragment = ContentsListFragment()
            val ft = supportFragmentManager.beginTransaction()
            ft.add(R.id.activity_infeed, fragment)
            ft.commit()
        }
    }
}