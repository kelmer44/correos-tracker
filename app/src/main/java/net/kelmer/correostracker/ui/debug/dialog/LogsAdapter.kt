package net.kelmer.correostracker.ui.debug.dialog

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import net.kelmer.correostracker.R
import net.kelmer.correostracker.di.debug.LumberYard

/**
 * Created by Gabriel Sanmartín on 07/10/2020.
 */
class LogsAdapter : RecyclerView.Adapter<LogsAdapter.ViewHolder>() {

    private var logs: MutableList<LumberYard.Entry> = mutableListOf()

    fun setLogs(logs: List<LumberYard.Entry>) {
        this.logs = logs.toMutableList()
        notifyDataSetChanged()
    }

    fun add(entry: LumberYard.Entry) {
        this.logs.add(entry)
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val levelView: TextView = itemView.findViewById(R.id.debug_log_level)
        private val tagView: TextView = itemView.findViewById(R.id.debug_log_tag)
        private val messageView: TextView = itemView.findViewById(R.id.debug_log_message)


        companion object {
            fun create(parent: ViewGroup): ViewHolder =
                    ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.listitem_debug_logs, parent, false))

        }

        fun bind(entry: LumberYard.Entry) {
            itemView.setBackgroundResource(backgroundForLevel(entry.level))
            itemView.setOnClickListener {
                val manager = itemView.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val data = ClipData.newPlainText("text", entry.message)
                manager.setPrimaryClip(data)
                Toast.makeText(itemView.context, "Text has been copied!", Toast.LENGTH_SHORT).show()
            }


            levelView.text = entry.displayLevel()
            tagView.text = entry.tag
            messageView.text = entry.message
        }

        @DrawableRes
        fun backgroundForLevel(level: Int): Int {
            return when (level) {
                Log.VERBOSE, Log.DEBUG -> {
                    R.color.debug_log_accent_debug
                }
                Log.INFO -> {
                    R.color.debug_log_accent_info
                }
                Log.WARN -> {
                    R.color.debug_log_accent_warn
                }
                Log.ERROR, Log.ASSERT -> {
                    R.color.debug_log_accent_error
                }
                else -> {
                    R.color.debug_log_accent_unknown
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder.create(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(logs[position])
    }

    override fun getItemCount(): Int = logs.size
}