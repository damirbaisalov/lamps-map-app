package kz.bfgroup.formmap.common

import android.app.Activity
import android.app.AlertDialog
import android.view.LayoutInflater
import kz.bfgroup.formmap.R

class LoadingDialog(
    private val activity: Activity
) {
    private lateinit var dialog: AlertDialog

    fun startLoadingDialog() {
        val builder : AlertDialog.Builder = AlertDialog.Builder(activity)
        val inflater : LayoutInflater = activity.layoutInflater

        builder.setView(inflater.inflate(R.layout.custom_progress_dialog, null))
        builder.setCancelable(false)

        dialog = builder.create()
        dialog.show()
    }

    fun dialogDismiss() {
        dialog.dismiss()
    }

}