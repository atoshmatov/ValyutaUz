package uz.toshmatov.currency.core.extensions

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun String.getFlagUrl(): String =
    "https://flagcdn.com/h80/${this.subSequence(0, 2).toString().lowercase()}.png"

fun String.toSom(): String = "$this so'm"

fun String.addPlus(): String = "+".plus(this)

fun String.toNumber(): String {
    val parts = this.split(".")
    val integerPart = parts[0]
    val decimalPart = parts.getOrElse(1) { "00" }
    val reversedIntegerPart = integerPart.reversed()
    val formattedReversedIntegerPart = reversedIntegerPart.chunked(3).joinToString(" ")
    val formattedIntegerPart = formattedReversedIntegerPart.reversed()
    val reversedIntegerPart1 = decimalPart.reversed()
    val formattedReversedIntegerPart1 = reversedIntegerPart1.chunked(3).joinToString(" ")
    val formattedIntegerPart1 = formattedReversedIntegerPart1.reversed()
    return "$formattedIntegerPart.$formattedIntegerPart1"
}

fun String.convertSomToDouble(): Double {
    val cleanedString = this
        .replace("so'm", "")
        .replace(" ", "")
        .replace(",", ".")
    return cleanedString.toDoubleOrNull() ?: 0.0
}

@SuppressLint("DefaultLocale")
fun Double.formatNumberDynamically(): String {
    return when {
        this >= 1 -> String.format("%.2f", this)
        this >= 0.01 -> String.format("%.4f", this)
        this >= 0.0001 -> String.format("%.6f", this)
        else -> String.format("%.8f", this).trimEnd('0').trimEnd(',')
    }
}

fun String.toNumber2(): String {
    val parts = this.split(",")
    val integerPart = parts[0]
    val decimalPart = parts.getOrElse(1) { "00" }
    val reversedIntegerPart = integerPart.reversed()
    val formattedReversedIntegerPart = reversedIntegerPart.chunked(3).joinToString(" ")
    val formattedIntegerPart = formattedReversedIntegerPart.reversed()
    val reversedIntegerPart1 = decimalPart.reversed()
    val formattedReversedIntegerPart1 = reversedIntegerPart1.chunked(3).joinToString(" ")
    val formattedIntegerPart1 = formattedReversedIntegerPart1.reversed()
    return "$formattedIntegerPart.$formattedIntegerPart1"
}



@RequiresApi(Build.VERSION_CODES.O)
fun String.formatDate(): String {
    val inputFormat = "MM/dd/yyyy"
    val outputFormat = "dd-MM-yyyy"
    val inputFormatter = DateTimeFormatter.ofPattern(inputFormat)
    val outputFormatter = DateTimeFormatter.ofPattern(outputFormat)

    val date = LocalDate.parse(this, inputFormatter)
    return date.format(outputFormatter)
}