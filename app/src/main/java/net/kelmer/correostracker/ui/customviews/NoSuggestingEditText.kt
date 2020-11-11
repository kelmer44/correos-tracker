package net.kelmer.correostracker.ui.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText

class NoSuggestingEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatEditText(context, attrs, defStyleAttr) {

    override fun getAutofillType(): Int {
        return View.AUTOFILL_TYPE_NONE
    }
}
