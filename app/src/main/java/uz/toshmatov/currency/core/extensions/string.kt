package uz.toshmatov.currency.core.extensions

fun String.getFlagUrl(): String =
    "https://flagcdn.com/h80/${this.subSequence(0, 2).toString().lowercase()}.png"
