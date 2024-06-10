package uz.toshmatov.currency.data.mapper.cbu

import uz.toshmatov.currency.core.extensions.toNumber
import uz.toshmatov.currency.core.extensions.toSom
import uz.toshmatov.currency.core.mapper.Mapper
import uz.toshmatov.currency.data.local.room.entity.CBUEntity
import uz.toshmatov.currency.domain.model.CBUModel
import java.util.Locale
import javax.inject.Inject

class CBUDaoMapper @Inject constructor() : Mapper<CBUModel, CBUEntity> {
    override fun mapToEntity(model: CBUModel): CBUEntity {
        TODO("Not yet implemented")
    }

    override fun mapFromEntity(entity: CBUEntity): CBUModel {
        return CBUModel(
            id = entity.id,
            code = entity.code,
            ccy = entity.currencyCode,
            nominal = entity.nominal,
            rate = entity.rate.toNumber().toSom(),
            diff = entity.difference,
            date = entity.date,
            ccyName = when (Locale.getDefault().language) {
                "uz" -> entity.currencyNameUZ
                "cr" -> entity.currencyNameUZC
                "en" -> entity.currencyNameEN
                "ru" -> entity.currencyNameRU
                else -> ""
            }
        )
    }
}