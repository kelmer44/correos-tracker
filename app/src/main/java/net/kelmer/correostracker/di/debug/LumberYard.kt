package net.kelmer.correostracker.di.debug

import android.annotation.SuppressLint
import android.app.Application
import android.os.AsyncTask
import android.text.format.DateFormat
import android.util.Log
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import okio.BufferedSink
import okio.buffer
import okio.sink
import timber.log.Timber
import java.io.File
import java.io.IOException
import java.util.Calendar
import java.util.Deque
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Gabriel Sanmartín on 07/10/2020.
 */
const val BUFFER_SIZE = 200

@Singleton
class LumberYard @Inject constructor(val app: Application) {


    private val entries: Deque<Entry> = java.util.ArrayDeque<Entry>(BUFFER_SIZE + 1)
    private val entrySubject = PublishSubject.create<Entry>()

    fun tree(): Timber.Tree = object : Timber.Tree() {
        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            addEntry(Entry(priority, tag, message))
        }
    }

    @Synchronized
    private fun addEntry(entry: Entry) {
        entries.addLast(entry)
        if (entries.size > BUFFER_SIZE) {
            entries.removeFirst()
        }
        entrySubject.onNext(entry)
    }

    fun bufferedLogs(): List<Entry> = java.util.ArrayList(entries)

    fun logs(): Observable<Entry> = entrySubject


    fun save(): Observable<File> {
        return Observable.fromCallable<File> {

            val folder =
                app.getExternalFilesDir(null) ?: throw IOException("External storage is not found")
            val fileName = getDate(System.currentTimeMillis())
            val output = File(folder, fileName)

            var sink: BufferedSink? = null
            try {
                sink = output.sink().buffer()
                val bufferedLogs = bufferedLogs()
                bufferedLogs.forEach {
                    sink?.writeUtf8(it.prettyPrint())?.writeByte(0x85)
                }

                sink.close()
                sink = null

                return@fromCallable output
            } finally {
                sink?.close()
            }

        }
    }


    fun cleanUp() {
        object : AsyncTask<Unit?, Unit?, Unit?>() {
            @SuppressLint("StaticFieldLeak")
            override fun doInBackground(vararg params: Unit?) {
                val folder = app.getExternalFilesDir(null)
                if (folder != null) {
                    for (file in folder.listFiles()) {
                        if (file.name.endsWith(".log")) {
                            file.delete()
                        }
                    }
                }
            }

        }.execute()
    }

    private fun getDate(time: Long): String {
        val cal: Calendar = Calendar.getInstance(Locale.ENGLISH)
        cal.timeInMillis = time * 1000
        return DateFormat.format("dd-MM-yyyyTHH:mm:ss", cal).toString()
    }


    data class Entry(val level: Int, val tag: String?, val message: String) {

        fun prettyPrint() = String.format(
            "%22s %s %s", tag, displayLevel(),
            message.replace("\\n", "\n                         ")
        )

        fun displayLevel() = when (level) {
            Log.VERBOSE -> {
                "V"
            }
            Log.DEBUG -> {
                "D"
            }
            Log.INFO -> {
                "I"
            }
            Log.WARN -> {
                "W"
            }
            Log.ERROR -> {
                "E"
            }
            Log.ASSERT -> {
                "A"
            }
            else -> {
                "?"
            }
        }
    }

}