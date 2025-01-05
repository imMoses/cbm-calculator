package id.cbm.main.cbm_calculator.utils // ktlint-disable package-name

import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.text.style.SubscriptSpan
import android.text.style.SuperscriptSpan

object HelperTextSpannable {

    fun subscriptText(
        rawText: String,
        startIndex: Int,
        endIndex: Int,
        proportionSize: Float = 0.7f,
    ): SpannableString {
        val text = SpannableString(rawText)
        text.setSpan(SubscriptSpan(), startIndex, endIndex, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
        text.setSpan(RelativeSizeSpan(proportionSize), startIndex, endIndex, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)

        return text
    }

    fun subscriptMultipleTextSingleLine(
        rawText: String,
        listLetter: List<LetterModel>? = null,
    ): SpannableString {
        val text = SpannableString(rawText)

        listLetter?.let { safeList ->
            if (safeList.isNotEmpty()) {
                safeList.forEach {
                    text.setSpan(
                        SubscriptSpan(),
                        it.startIndex,
                        it.endIndex,
                        SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE,
                    )
                    text.setSpan(
                        RelativeSizeSpan(it.proportionSize),
                        it.startIndex,
                        it.endIndex,
                        SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE,
                    )
                }
            }
        }

        return text
    }

    fun combineSubscriptSuperscriptLetter(
        rawText: String,
        listLetter: List<LetterModel>? = null,
    ): SpannableString {
        val text = SpannableString(rawText)

        listLetter?.let { safeList ->
            if (safeList.isNotEmpty()) {
                safeList.forEach {
                    when (it.type) {
                        Constants.SUBSCRIPT_LETTER -> {
                            text.setSpan(
                                SubscriptSpan(),
                                it.startIndex,
                                it.endIndex,
                                SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE,
                            )
                            text.setSpan(
                                RelativeSizeSpan(it.proportionSize),
                                it.startIndex,
                                it.endIndex,
                                SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE,
                            )
                        }
                        Constants.SUPERSCRIPT_LETTER -> {
                            text.setSpan(
                                SuperscriptSpan(),
                                it.startIndex,
                                it.endIndex,
                                SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE,
                            )
                            text.setSpan(
                                RelativeSizeSpan(it.proportionSize),
                                it.startIndex,
                                it.endIndex,
                                SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE,
                            )
                        }
                    }
                }
            }
        }

        return text
    }
}
