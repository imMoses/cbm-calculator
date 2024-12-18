package id.cbm.main.cbm_calculator.utils // ktlint-disable package-name

import android.os.SystemClock
import android.util.Log
import android.view.View
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
    val defaultInterval = 1000
    var lastTimeClicked = 0L

    setOnClickListener {
        if (SystemClock.elapsedRealtime() - lastTimeClicked < defaultInterval) {
            return@setOnClickListener
        }

        lastTimeClicked = SystemClock.elapsedRealtime()
        onSafeClick(it)
    }
}

fun String.stringToDate(dateFormat: String): Date? {
    var date: Date? = null

    val sdf = SimpleDateFormat(dateFormat, Locale.US)
    date = sdf.parse(this)

    return date
}

fun Date.dateToString(dateFormat: String? = null): String {
    // by default below
    var _dateFormat = "dd MMM yyyy"

    // if dateFormat argument not null, just replace with custom format
    if (dateFormat != null) {
        _dateFormat = dateFormat
    }

    try {
        val sdf = SimpleDateFormat(_dateFormat, Locale.US)

        return sdf.format(this)
    } catch (e: Exception) {
        Log.e("Extensions", e.message.toString())
        return "-"
    }
}
