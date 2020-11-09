package net.kelmer.correostracker.ui.debug.dialog

import android.app.AlertDialog
import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import net.kelmer.correostracker.di.debug.LumberYard

/**
 * Created by Gabriel Sanmartín on 07/10/2020.
 */
class LogsDialog(context: Context, private val lumberYard: LumberYard) : AlertDialog(context) {

    val adapter: LogsAdapter = LogsAdapter()

    private val disposables: CompositeDisposable = CompositeDisposable()

    init {

        val recyclerView = RecyclerView(context)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        setTitle("Logs")
        setView(recyclerView)
        setButton(BUTTON_NEGATIVE, "Close") { d, which ->

        }

        setButton(BUTTON_POSITIVE, "Share") { d, which ->
            share()
        }

    }

    override fun onStart() {
        super.onStart()
        adapter.setLogs(lumberYard.bufferedLogs())
        disposables.add(
                lumberYard.logs()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(adapter::add)
        )
    }

    override fun onStop() {
        super.onStop()
        disposables.clear()
    }

    private fun share() {


    }
}