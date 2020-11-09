package net.kelmer.correostracker.ui.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.Module
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.coroutines.test.withTestContext
import net.kelmer.correostracker.R
import net.kelmer.correostracker.base.activity.BaseActivity
import net.kelmer.correostracker.di.activity.ActivityModule
import net.kelmer.correostracker.ui.detail.DetailFragment.Companion.KEY_PARCELCODE
import net.kelmer.correostracker.ui.list.ParcelListActivity

/**
 * Created by gabriel on 25/03/2018.
 */
@AndroidEntryPoint
class DetailActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val code = intent.getStringExtra(KEY_PARCELCODE)
        if (code != null) {
            supportFragmentManager.beginTransaction().add(
                    R.id.detail_content, DetailFragment.newInstance(code)).commit()
        }
    }

    companion object {

        fun newIntent(context: Context, code: String): Intent =
                Intent(context, DetailActivity::class.java).apply {
                    putExtra(KEY_PARCELCODE, code)
                }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}