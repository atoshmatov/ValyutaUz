package uz.toshmatov.currency.core.html

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.jsoup.Connection
import org.jsoup.Jsoup
import java.io.IOException


fun main() = runBlocking(Dispatchers.IO) {
    val exchangeRates = getExchangeRates()
    println(exchangeRates)
}

fun getExchangeRates(): JsonObject {
    val url = "https://dollaruz.pw" // URL of the webpage

    return runBlocking(Dispatchers.IO) {
        try {
            val session: Connection = Jsoup.newSession()
                .timeout(20 * 1000)
                .userAgent("FooBar 2000").url(url)
            val doc = session.newRequest().get()

            val table = doc.select(".banks table")

            val jsonArray = JsonArray()

            table.select("tbody tr").forEach { row ->
                val bank = row.select("td:eq(0)").text()
                val buy = row.select("td.buy").text()
                val sell = row.select("td.sell").text()
                val date = row.select("td.datetime").text()

                val jsonObj = JsonObject()
                jsonObj.addProperty("bank", bank)
                jsonObj.addProperty("buy", buy)
                jsonObj.addProperty("sell", sell)
                jsonObj.addProperty("date", date)

                jsonArray.add(jsonObj)
            }

            val resultJson = JsonObject()
            resultJson.add("exchange_rates", jsonArray)

            return@runBlocking resultJson

        } catch (e: IOException) {
            e.printStackTrace()
            return@runBlocking JsonObject()
        }
    }
}