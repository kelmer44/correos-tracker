package net.kelmer.correostracker.ui.list

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import net.kelmer.correostracker.R
import net.kelmer.correostracker.base.activity.BaseActivity
import net.kelmer.correostracker.ui.create.CreateActivity

class ParcelListActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)

        fab.setOnClickListener {
            startActivityForResult(CreateActivity.newIntent(this),
                    REQ_CREATE_PARCEL)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_CREATE_PARCEL && resultCode == Activity.RESULT_OK) {
            for (fragment in supportFragmentManager.fragments) {
                fragment.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    companion object {
        const val REQ_CREATE_PARCEL = 101
    }
}
