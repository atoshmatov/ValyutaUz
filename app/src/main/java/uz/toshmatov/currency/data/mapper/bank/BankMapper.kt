package uz.toshmatov.currency.data.mapper.bank

import com.google.gson.JsonObject
import uz.toshmatov.currency.core.extensions.toSom
import uz.toshmatov.currency.core.mapper.Mapper
import uz.toshmatov.currency.domain.model.ExchangeBankModel
import javax.inject.Inject

class BankMapper @Inject constructor() : Mapper<ExchangeBankModel, JsonObject> {
    override fun mapToEntity(model: ExchangeBankModel): JsonObject {
        TODO("Not yet implemented")
    }

    override fun mapFromEntity(entity: JsonObject): ExchangeBankModel {
        return ExchangeBankModel(
            bank = entity.asJsonObject.get("bank").asString,
            buy = entity.asJsonObject.get("buy").asString.toSom(),
            sell = entity.asJsonObject.get("sell").asString.toSom(),
            date = entity.asJsonObject.get("date").asString
        )
    }
}