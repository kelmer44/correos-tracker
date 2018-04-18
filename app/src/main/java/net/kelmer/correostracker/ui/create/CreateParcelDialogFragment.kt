package net.kelmer.correostracker.ui.create

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import kotlinx.android.synthetic.main.activity_create.*
import net.kelmer.correostracker.R
import net.kelmer.correostracker.base.dialog.BaseDialog
import net.kelmer.correostracker.base.dialog.DialogsManager

class CreateParcelDialogFragment : BaseDialog() {


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        isCancelable = true


        var inflate = LayoutInflater.from(activity.applicationContext)
                .inflate(R.layout.fragment_create_parcel, null, false)


        return super.onCreateDialog(savedInstanceState)
    }
}