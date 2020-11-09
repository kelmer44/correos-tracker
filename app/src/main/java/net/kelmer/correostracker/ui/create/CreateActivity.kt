package net.kelmer.correostracker.ui.create

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.Module
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_create.*
import net.kelmer.correostracker.R
import net.kelmer.correostracker.base.activity.BaseActivity
import net.kelmer.correostracker.di.activity.ActivityModule
import net.kelmer.correostracker.ui.detail.DetailActivity

/**
 * Created by gabriel on 25/03/2018.
 */
@AndroidEntryPoint
class CreateActivity : BaseActivity(R.layout.activity_create) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }


    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, CreateActivity::class.java)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}