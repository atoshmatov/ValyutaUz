package uz.toshmatov.currency.data.mapper.bank

import com.google.gson.JsonObject
import uz.toshmatov.currency.core.mapper.Mapper
import uz.toshmatov.currency.data.local.room.entity.BanksEntity
import javax.inject.Inject

class BankNetMapper @Inject constructor() : Mapper<JsonObject, BanksEntity> {
    override fun mapToEntity(model: JsonObject): BanksEntity {
        return BanksEntity(
            bank = model.asJsonObject.get("bank").asString,
            buy = model.asJsonObject.get("buy").asString,
            sell = model.asJsonObject.get("sell").asString,
            date = model.asJsonObject.get("date").asString
        )
    }

    override fun mapFromEntity(entity: BanksEntity): JsonObject {
        TODO("Not yet implemented")
    }
}