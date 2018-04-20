package net.kelmer.correostracker.ui.customview

import android.content.Context
import android.support.v7.widget.AppCompatEditText
import android.util.AttributeSet
import android.view.View

class NoSuggestingEditText @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatEditText(context, attrs, defStyleAttr) {

    override fun getAutofillType(): Int {
        return View.AUTOFILL_TYPE_NONE
    }
}