package uz.toshmatov.currency.core.extensions

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.core.net.toUri


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

fun Context.openRateAppPage() {
    val packageName = this.packageName
    val marketIntent = Intent(
        Intent.ACTION_VIEW,
        "market://details?id=uz.toshmatov.currency".toUri()
    )

    try {
        this.startActivity(marketIntent)
    } catch (_: ActivityNotFoundException) {
        val webIntent = Intent(
            Intent.ACTION_VIEW,
            "https://play.google.com/store/apps/details?id=uz.toshmatov.currency".toUri()
        )
        this.startActivity(webIntent)
    }
}

fun Context.makeToast(log: CharSequence) {
    Toast.makeText(this, log, Toast.LENGTH_LONG).show()
}
