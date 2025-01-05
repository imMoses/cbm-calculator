package id.cbm.main.cbm_calculator.utils // ktlint-disable package-name

data class LetterModel(
    val startIndex: Int,
    val endIndex: Int,
    val proportionSize: Float = 0.7f,
    val type: Int = Constants.SUBSCRIPT_LETTER
)
