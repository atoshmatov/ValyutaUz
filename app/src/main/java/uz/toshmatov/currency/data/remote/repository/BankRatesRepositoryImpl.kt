package uz.toshmatov.currency.data.remote.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import uz.toshmatov.currency.domain.model.BankRateModel
import uz.toshmatov.currency.domain.repository.BankRatesRepository
import javax.inject.Inject

class BankRatesRepositoryImpl @Inject constructor(
    private val okHttpClient: OkHttpClient
) : BankRatesRepository {

    override suspend fun getBankRates(currency: String): List<BankRateModel> =
        withContext(Dispatchers.IO) {
            val url = "https://kurs.uz/oz/data/currencies?by_bank=all&by_currency=$currency&sort_by=buy"
            val request = Request.Builder().url(url)
                .addHeader("X-Requested-With", "XMLHttpRequest")
                .build()
            val html = okHttpClient.newCall(request).execute().use { it.body?.string() ?: "" }
            parseHtml(html, currency)
        }

    private fun parseHtml(html: String, currency: String): List<BankRateModel> {
        val pattern = Regex(
            """href="/oz/banks/([^/\"]+)/[^\"]+">.*?<span>([^<]+)</span>.*?data-curr="(\d+)".*?data-curr="(\d+)"""",
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