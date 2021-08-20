package kz.bfgroup.formmap.requests_all.view

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class SuccessSubmitRequestDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Успешная заявка!")
                .setNeutralButton("ОК") {
                        dialog, id ->  dialog.cancel()
                    activity?.finish()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}