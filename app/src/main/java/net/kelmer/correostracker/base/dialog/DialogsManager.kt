package net.kelmer.correostracker.base.dialog

import android.os.Bundle
import android.support.annotation.UiThread
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.text.TextUtils


@UiThread
class DialogsManager(private val fragmentManager: FragmentManager) {

    companion object {
        /**
         * Whenever a dialog is shown with non-empty "id", the provided id will be stored in
         * arguments Bundle under this key.
         */
        const val ARGUMENT_DIALOG_ID = "ARGUMENT_DIALOG_ID"

        /**
         * In case Activity or Fragment that instantiated this DialogsManager are re-created (e.g.
         * in case of memory reclaim by OS, orientation change, etc.), we need to be able
         * to get a reference to dialog that might have been shown. This tag will be supplied with
         * all DialogFragment's shown by this DialogsManager and can be used to query
         * [FragmentManager] for last shown dialog.
         */
        const val DIALOG_FRAGMENT_TAG = "DIALOG_FRAGMENT_TAG"

    }

    var currentlyShownDialog: DialogFragment? = null

    init {
        //there might be some dialog already shown
        var fragmentWithDialogTag = fragmentManager.findFragmentByTag(DIALOG_FRAGMENT_TAG)
        if (fragmentWithDialogTag != null
                && fragmentWithDialogTag is DialogFragment) {
            currentlyShownDialog = fragmentWithDialogTag
        }
    }

    /**
     * Obtain the id of the currently shown dialog.
     * @return the id of the currently shown dialog; null if no dialog is shown, or the currently
     *         shown dialog has no id
     */
    fun getCurrentlyShownDialogId(): String? {
        var dialog = currentlyShownDialog
        var id = dialog?.arguments?.containsKey(ARGUMENT_DIALOG_ID) ?: false
        return if (dialog != null && dialog.arguments != null ||
                !id) {
            null
        } else {
            dialog?.arguments?.getString(ARGUMENT_DIALOG_ID)
        }
    }

    /**
     * Check whether a dialog with a specified id is currently shown
     * @param id dialog id to query
     * @return true if a dialog with the given id is currently shown; false otherwise
     */
    fun isDialogCurrentlyShown(id: String): Boolean {
        var shownDialogId = getCurrentlyShownDialogId()
        return !TextUtils.isEmpty(shownDialogId) && shownDialogId.equals(id)
    }

    /**
     * Dismiss the currently shown dialog. Has no effect if no dialog is shown. Please note that
     * we always allow state loss upon dismissal.
     */
    fun dismissCurrentlyShownDialog() {
        currentlyShownDialog?.dismissAllowingStateLoss()
        currentlyShownDialog = null
    }

    /**
     * Show dialog and assign it a given "id". Replaces any other currently shown dialog.<br>
     * The shown dialog will be retained across parent activity re-creation.
     * @param dialog dialog to show
     * @param id string that uniquely identifies the dialog; can be null
     */
    fun showRetainedDialogWithId(dialog: DialogFragment, id: String) {
        dismissCurrentlyShownDialog()
        dialog.retainInstance = true
        setId(dialog, id)
        showDialog(dialog)
    }

    fun setId(dialog: DialogFragment, id: String) {
        var args = if (dialog.arguments != null) dialog.arguments else Bundle(1)
        args.putString(ARGUMENT_DIALOG_ID, id)
        dialog.arguments = args
    }

    fun showDialog(dialog: DialogFragment) {
        dialog.show(fragmentManager, DIALOG_FRAGMENT_TAG)
        currentlyShownDialog = dialog
    }
}