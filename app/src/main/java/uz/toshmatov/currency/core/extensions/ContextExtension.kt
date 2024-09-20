package uz.toshmatov.currency.core.extensions

import android.content.Context
import android.content.Intent
import android.net.Uri


fun Context.openEmail() {
    val intent = Intent(
        Intent.ACTION_SENDTO,
        Uri.parse("mailto:a.toshmatov.dev@gmail.com")
    )
    intent.putExtra(Intent.EXTRA_SUBJECT, "subject")
    intent.putExtra(Intent.EXTRA_TEXT, "body")
    this.startActivity(Intent.createChooser(intent, "Email"))
}

fun Context.openShareAppLink() {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(
            Intent.EXTRA_TEXT,
            "Valyuta uz \nhttps://play.google.com/store/apps/details?id=uz.toshmatov.currency"
        )
        type = "text/plain"
    }

    val shareIntent = Intent.createChooser(sendIntent, null)
    this.startActivity(shareIntent)
}

fun Context.telegramIntent() {
    val telegram = Intent(
        Intent.ACTION_VIEW,
        Uri.parse("https://t.me/a_toshmatov")
    )
    telegram.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    telegram.setPackage("org.telegram.messenger")
    this.startActivity(telegram)
}