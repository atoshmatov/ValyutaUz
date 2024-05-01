package uz.toshmatov.currency.data.mapper

import com.google.gson.JsonObject
import uz.toshmatov.currency.core.mapper.Mapper
import uz.toshmatov.currency.domain.model.ExchangeModel
import javax.inject.Inject

class ExchangeMapper @Inject constructor() : Mapper<ExchangeModel, JsonObject> {
    override fun mapToEntity(model: ExchangeModel): JsonObject {
        TODO("Not yet implemented")
    }

    override fun mapFromEntity(entity: JsonObject): ExchangeModel {
        return ExchangeModel(
            bank = entity.asJsonObject.get("bank").asString,
            buy = entity.asJsonObject.get("buy").asString,
            sell = entity.asJsonObject.get("sell").asString,
            date = entity.asJsonObject.get("date").asString
        )
    }
}