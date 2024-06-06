package uz.toshmatov.currency.data.mapper.cbu

import uz.toshmatov.currency.core.extensions.toNumber
import uz.toshmatov.currency.core.extensions.toSom
import uz.toshmatov.currency.core.mapper.Mapper
import uz.toshmatov.currency.data.remote.model.CBUDto
import uz.toshmatov.currency.domain.model.CBUModel
import java.util.Locale
import javax.inject.Inject

class CBUMapper @Inject constructor() : Mapper<CBUModel, CBUDto> {
    override fun mapToEntity(model: CBUModel): CBUDto {
        return CBUDto(
            id = model.id.toInt(),
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

    override fun mapFromEntity(entity: CBUDto): CBUModel {
        return CBUModel(
            id = entity.id.toLong(),
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