package com.interview.tmdb_mvc.utility

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.Window
import android.view.inputmethod.InputMethodManager
import com.interview.tmdb_mvc.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object Util {

    /* * @ Function Name     : showLoading
      * @ Function Params    : 1) activity: (Activity) using for current activity reference
      *
      * @ Return Value       : Dialog
      * @ Function Purpose   : To show progressBar in dialog while API call
      * */

    fun showLoading(activity: Activity?): Dialog? {
        if (activity != null) {
            hideKeyboard(activity)
        }
        val dialog = Dialog(activity!!)
        val window = dialog.window!!
        window.setBackgroundDrawableResource(android.R.color.transparent)
        window.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.loder)
        dialog.setCancelable(false)
        dialog.show()
        dialog.setOnDismissListener { dialog.dismiss() }
        return dialog
    }

    private fun hideKeyboard(ctx: Context) {
        val inputManager =
            ctx.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val v = (ctx as Activity).currentFocus ?: return
        inputManager.hideSoftInputFromWindow(v.windowToken, 0)
    }

    fun parseDateToddMMyyyy(time: String?): String? {
        val inputPattern = "yyyy-MM-dd"
        val outputPattern = "dd MMM yyyy"
        val inputFormat = SimpleDateFormat(inputPattern)
        val outputFormat = SimpleDateFormat(outputPattern)
        var date: Date? = null
        var str: String? = null
        try {
            date = inputFormat.parse(time)
            str = outputFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return str
    }

}