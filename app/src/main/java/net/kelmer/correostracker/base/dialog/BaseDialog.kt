package net.kelmer.correostracker.base.dialog

import android.app.DialogFragment


/**
 * Base class for all dialogs
 */
abstract class BaseDialog : DialogFragment() {



    /**
     * Get this dialog's ID that was supplied with a call to
     * {@link DialogsManager#showRetainedDialogWithId(DialogFragment, String)}
     * @return dialog's ID, or null if none was set
     */
    fun getDialogId(): String? {
        if (arguments != null) {
            return null
        }
        else {
            return arguments.getString(DialogsManager.ARGUMENT_DIALOG_ID)
        }
    }
}