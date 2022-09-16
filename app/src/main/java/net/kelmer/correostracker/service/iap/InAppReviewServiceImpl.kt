package net.kelmer.correostracker.service.iap

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.firebase.crashlytics.FirebaseCrashlytics
import net.kelmer.correostracker.R
import net.kelmer.correostracker.data.prefs.SharedPrefsManager
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

class InAppReviewServiceImpl @Inject constructor(
    val activity: FragmentActivity,
    private val sharedPreferences: SharedPrefsManager
) : InAppReviewService {

    @SuppressLint("InflateParams")
    fun inAppReviewDialog(
        context: Context,
        listener: () -> Unit
    ): AlertDialog {
        // setup the alert builder
        val builder = AlertDialog.Builder(context)
        builder.setTitle(R.string.inapp_title)

        builder.setMessage(R.string.inapp_message)
        builder.setPositiveButton(
            android.R.string.ok
        ) { _, _ -> listener() }

        builder.setNegativeButton(
            android.R.string.cancel
        ) { _, _ -> }

        // create and show the alert dialog
        return builder.create()
    }

    override fun showIfNeeded() {
        if (sharedPreferences.getCleanStarts() > MIN_CLEAN_STARTS && !sharedPreferences.wasAskedForReview()) {
            inAppReviewDialog(activity) {
                val manager = ReviewManagerFactory.create(activity)
//                val manager = FakeReviewManager(activity)
                val request = manager.requestReviewFlow()
                request.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // We got the ReviewInfo object
                        val reviewInfo = task.result

                        val flow = manager.launchReviewFlow(activity, reviewInfo)
                        flow.addOnCompleteListener { result ->
                            // The flow has finished. The API does not indicate whether the user
                            // reviewed or not, or even whether the review dialog was shown. Thus, no
                            // matter the result, we continue our app flow.
                            Timber.i("Successful! - $result")
                        }
                    } else {
                        // There was some problem, log or handle the error code.
//                    @ReviewErrorCode val reviewErrorCode = (task.getException() as TaskException).errorCode
                        val e = task.exception
                        if (e != null) {
                            FirebaseCrashlytics.getInstance().recordException(e)
                        }
                    }
                }

            }.show().also { sharedPreferences.markAskedForReview() }
        }
    }

    companion object {
        const val MIN_CLEAN_STARTS = 5
    }
}
