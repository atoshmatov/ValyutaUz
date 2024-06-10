package uz.toshmatov.currency.data.remote.api.impl

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.jsoup.Connection
import uz.toshmatov.currency.data.remote.api.ExchangeBankDataSource
import java.io.IOException
import javax.inject.Inject

class ExchangeBankDataSouImpl @Inject constructor(
    private val session: Connection
) : ExchangeBankDataSource {

    override fun exchangeBankData(): Flow<JsonObject> {
        return flow {
            try {
                val doc = session.newRequest().get()
                val table = doc.select(".banks table")
                val jsonArray = JsonArray()

                table.select("tbody tr").forEach { row ->
                    val bank = row.select("td:eq(0)").text()
                    val buy = row.select("td.buy").text()
                    val sell = row.select("td.sell").text()
                    val date = row.select("td.datetime").text()

                    val jsonObj = JsonObject().apply {
                        addProperty("bank", bank)
                        addProperty("buy", buy)
                        addProperty("sell", sell)
                        addProperty("date", date)
                    }
                    jsonArray.add(jsonObj)
                }

                val resultJson = JsonObject()
                resultJson.add("exchange_rates", jsonArray)

                emit(resultJson)

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}