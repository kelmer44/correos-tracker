package net.kelmer.correostracker.iar

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import net.kelmer.correostracker.dataApi.LazySharedPreferences
import timber.log.Timber
import javax.inject.Inject

class InAppSharedPreferences @Inject constructor(@ApplicationContext context: Context) :
    LazySharedPreferences(
        context,
        IAP_PREFS_KEY
    ) {

    init {
        if (this.getCleanStarts() <= MIN_CLEAN_STARTS) {
            incrementCleanStarts()
            Timber.w("Incrementing clean starts!, value after is = ${this.getCleanStarts()}")
        }
    }

    fun getCleanStarts(): Int = get(CLEAN_STARTS, 0)

    private fun incrementCleanStarts() {
        set(CLEAN_STARTS, getCleanStarts() + 1)
    }

    fun wasAskedForReview(): Boolean = get(ASKED_FOR_REVIEW, false)

    fun markAskedForReview() {
        set(ASKED_FOR_REVIEW, true)
    }

    companion object {
        const val MIN_CLEAN_STARTS = 5
        const val IAP_PREFS_KEY = "IAP"
        private const val CLEAN_STARTS = "I_CLEAN_STARTS"
        private const val ASKED_FOR_REVIEW = "B_ASKED_FOR_REVIEW"
    }
}
