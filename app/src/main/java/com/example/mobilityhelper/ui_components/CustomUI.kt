package com.example.mobilityhelper.ui_components

import android.app.AlertDialog
import android.view.View

class CustomUI {

    fun createDialog(view: View, title: String, message: String, icon: Int) {
        val alertBuilder = AlertDialog.Builder(view.context)

        alertBuilder.setTitle(title)
        alertBuilder.setMessage(message)
        alertBuilder.setIcon(icon)
        alertBuilder.setPositiveButton("Okay") { dialogInterface, which ->
        }

        val alertDialog: AlertDialog = alertBuilder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }
}