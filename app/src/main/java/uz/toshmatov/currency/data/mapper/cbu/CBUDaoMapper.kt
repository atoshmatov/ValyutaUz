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
        return CBUEntity(
            id = model.id,
            code = model.code,
            currencyCode = model.ccy,
            nominal = model.nominal,
            rate = model.rate.toNumber(),
            difference = model.diff,
            date = model.date,
            currencyNameUZ = model.ccyName,
            currencyNameUZC = model.ccyName,
            currencyNameEN = model.ccyName,
            currencyNameRU = model.ccyName
        )
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