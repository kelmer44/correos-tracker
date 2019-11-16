package net.kelmer.correostracker.ui.list

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.crashlytics.android.Crashlytics
import kotlinx.android.synthetic.main.activity_main.*
import net.kelmer.correostracker.R
import net.kelmer.correostracker.ui.create.CreateActivity

class ParcelListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)

        fab.setOnClickListener {
            startActivity(CreateActivity.newIntent(this))
        }
    }
}
