package uz.toshmatov.currency.data.mapper

import uz.toshmatov.currency.core.mapper.Mapper
import uz.toshmatov.currency.data.remote.model.CBUDto
import uz.toshmatov.currency.domain.model.CBUModel
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
            ccyNmRU = entity.currencyNameRU,
            ccyNmUZ = entity.currencyNameUZ,
            ccyNmUZC = entity.currencyNameUZC,
            ccyNmEN = entity.currencyNameEN,
            nominal = entity.nominal,
            rate = entity.rate,
            diff = entity.difference,
            date = entity.date
        )
    }
}