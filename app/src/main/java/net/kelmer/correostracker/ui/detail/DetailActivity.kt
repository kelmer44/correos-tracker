package net.kelmer.correostracker.ui.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*
import net.kelmer.correostracker.R

/**
 * Created by gabriel on 25/03/2018.
 */
class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }


    companion object {
        val KEY_PARCELCODE = "PARCEL_CODE"

        fun newIntent(context: Context, code: String): Intent {
            var intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(KEY_PARCELCODE, code)
            return intent
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}