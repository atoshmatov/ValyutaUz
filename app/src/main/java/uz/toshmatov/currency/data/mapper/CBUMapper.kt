package uz.toshmatov.currency.data.mapper

import uz.toshmatov.currency.core.extensions.toNumber
import uz.toshmatov.currency.core.extensions.toSom
import uz.toshmatov.currency.core.mapper.Mapper
import uz.toshmatov.currency.data.remote.model.CBUDto
import uz.toshmatov.currency.domain.model.CBUModel
import java.util.Locale
import javax.inject.Inject

class CBUMapper @Inject constructor() : Mapper<CBUModel, CBUDto> {
    override fun mapToEntity(model: CBUModel): CBUDto {
        TODO("Not yet implemented")
    }

    override fun mapFromEntity(entity: CBUDto): CBUModel {
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