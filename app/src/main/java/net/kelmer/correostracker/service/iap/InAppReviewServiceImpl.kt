package net.kelmer.correostracker.service.iap

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import com.google.android.play.core.review.testing.FakeReviewManager
import net.kelmer.correostracker.R
import timber.log.Timber
import javax.inject.Inject

class InAppReviewServiceImpl @Inject constructor(
    val activity: FragmentActivity,
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

        // create and show the alert dialog
        return builder.create()
    }

    override fun showIfNeeded() {
        inAppReviewDialog(activity) {

            val manager = FakeReviewManager(activity)
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
                    Timber.i("Error ${task.exception}")

                }
            }
        }.show()
    }

}
