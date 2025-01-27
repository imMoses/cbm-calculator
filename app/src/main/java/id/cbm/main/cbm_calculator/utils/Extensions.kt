package id.cbm.main.cbm_calculator.utils // ktlint-disable package-name

import android.os.SystemClock
import android.util.Log
import android.view.View
import java.lang.Exception
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
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

fun String.toIDR(withCurrency: Boolean = false): String {
    return try {
        val number = this.toDouble()

        // define custom decimal
        val symbols = DecimalFormatSymbols(Locale.US).apply {
            groupingSeparator = '.'
            decimalSeparator = ','
        }

        // create decimal format with defined symbols
        val decimalFormat = if (number % 1 == 0.0) {
            DecimalFormat("#,##0", symbols)
        } else {
            DecimalFormat("#,##0.00", symbols)
        }

        if (withCurrency) "Rp ${decimalFormat.format(number)}" else decimalFormat.format(number)
    } catch (e: NumberFormatException) {
        e.printStackTrace()
        "0"
    } catch (e: IllegalArgumentException) {
        e.printStackTrace()
        "0"
    }
}
