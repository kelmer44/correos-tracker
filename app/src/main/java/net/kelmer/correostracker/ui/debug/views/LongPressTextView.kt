package net.kelmer.correostracker.ui.debug.views

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.AttributeSet
import android.widget.Toast

/**
 * Created by Gabriel Sanmartín on 11/09/2020.
 */
class LongPressTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatTextView(context, attrs, defStyleAttr) {

    init {

        this.setOnLongClickListener {
            val manager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val data = ClipData.newPlainText("text", text)
            manager.setPrimaryClip(data)
            Toast.makeText(context, "Text has been copied!", Toast.LENGTH_SHORT).show()
            true
        }
    }
}
