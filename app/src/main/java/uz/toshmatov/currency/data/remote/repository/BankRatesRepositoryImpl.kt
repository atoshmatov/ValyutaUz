package uz.toshmatov.currency.data.remote.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import uz.toshmatov.currency.domain.model.BankRateModel
import uz.toshmatov.currency.domain.repository.BankRatesRepository
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class BankRatesRepositoryImpl @Inject constructor() : BankRatesRepository {

    private val client = OkHttpClient.Builder()
        .followRedirects(true)
        .followSslRedirects(true)
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    override suspend fun getBankRates(currency: String): List<BankRateModel> =
        withContext(Dispatchers.IO) {
            val url = "https://kurs.uz/oz/data/currencies?by_bank=all&by_currency=$currency&sort_by=buy"
            val request = Request.Builder()
                .url(url)
                .addHeader("X-Requested-With", "XMLHttpRequest")
                .addHeader("Accept", "text/html")
                .addHeader("User-Agent", "Mozilla/5.0 (Android)")
                .build()

            val html = client.newCall(request).execute().use { it.body?.string() ?: "" }
            parseHtml(html, currency)
        }

    private fun parseHtml(html: String, currency: String): List<BankRateModel> {
        if (html.isBlank()) return emptyList()
        val pattern = Regex(
            """banks/(\w+)/\w+[^>]*>.*?<span>([^<]+)</span>.*?data-curr="(\d+)".*?data-curr="(\d+)"""",
            RegexOption.DOT_MATCHES_ALL
        )
        return pattern.findAll(html).map { match ->
            BankRateModel(
                bankSlug = match.groupValues[1],
                bankName = match.groupValues[2].trim(),
                buy = match.groupValues[3].toIntOrNull() ?: 0,
                sell = match.groupValues[4].toIntOrNull() ?: 0,
                currency = currency
            )
        }.filter { it.buy > 0 && it.sell > 0 }.toList()
    }
}
