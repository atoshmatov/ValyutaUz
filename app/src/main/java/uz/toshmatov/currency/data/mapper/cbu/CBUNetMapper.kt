package uz.toshmatov.currency.data.mapper.cbu

import uz.toshmatov.currency.core.extensions.toNumber
import uz.toshmatov.currency.core.mapper.Mapper
import uz.toshmatov.currency.data.local.room.entity.CBUEntity
import uz.toshmatov.currency.data.remote.model.CBUDto
import javax.inject.Inject

class CBUNetMapper @Inject constructor() : Mapper<CBUDto, CBUEntity> {
    override fun mapToEntity(model: CBUDto): CBUEntity {
        return CBUEntity(
            code = model.code,
            currencyCode = model.currencyCode,
            nominal = model.nominal,
            rate = model.rate.toNumber(),
            difference = model.difference,
            date = model.date,
            currencyNameUZ = model.currencyNameUZ,
            currencyNameUZC = model.currencyNameUZC,
            currencyNameEN = model.currencyNameEN,
            currencyNameRU = model.currencyNameRU
        )
    }

    override fun mapFromEntity(entity: CBUEntity): CBUDto {
        TODO("Not yet implemented")
    }
}