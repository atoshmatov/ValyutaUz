package uz.toshmatov.currency.widget

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import androidx.core.content.edit

data class WidgetRateItem(
    val code: String,
    val codeName: String,
    val rate: String,
    val diff: String
)

object CurrencyWidgetStorage {
    private const val PREF_NAME = "currency_widget_storage"
    private const val KEY_PREFIX = "widget_rates_"
    private const val KEY_CODE = "code"
    private const val KEY_CODE_NAME = "code_name"
    private const val KEY_RATE = "rate"
    private const val KEY_DIFF = "diff"

    fun saveRates(
        context: Context,
        appWidgetId: Int,
        rates: List<WidgetRateItem>
    ) {
        val jsonArray = JSONArray()
        rates.forEach { item ->
            val jsonItem = JSONObject()
                .put(KEY_CODE, item.code)
                .put(KEY_CODE_NAME, item.codeName)
                .put(KEY_RATE, item.rate)
                .put(KEY_DIFF, item.diff)
            jsonArray.put(jsonItem)
        }
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit {
                putString(KEY_PREFIX + appWidgetId, jsonArray.toString())
            }
    }

    fun readRates(
        context: Context,
        appWidgetId: Int
    ): List<WidgetRateItem> {
        val raw = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getString(KEY_PREFIX + appWidgetId, null)
            ?: return emptyList()

        return runCatching {
            val jsonArray = JSONArray(raw)
            buildList {
                repeat(jsonArray.length()) { index ->
                    val item = jsonArray.getJSONObject(index)
                    add(
                        WidgetRateItem(
                            code = item.optString(KEY_CODE),
                            codeName = item.optString(KEY_CODE_NAME).ifBlank {
                                item.optString(KEY_CODE)
                            },
                            rate = item.optString(KEY_RATE),
                            diff = item.optString(KEY_DIFF)
                        )
                    )
                }
            }
        }.getOrDefault(emptyList())
    }

    fun clearRates(
        context: Context,
        appWidgetId: Int
    ) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit {
                remove(KEY_PREFIX + appWidgetId)
            }
    }
}
