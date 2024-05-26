package uz.toshmatov.currency.data.mapper.localmapper

import uz.toshmatov.currency.core.extensions.toNumber
import uz.toshmatov.currency.core.extensions.toSom
import uz.toshmatov.currency.core.mapper.Mapper
import uz.toshmatov.currency.data.local.room.entity.CBUEntity
import uz.toshmatov.currency.data.remote.model.CBUDto
import javax.inject.Inject

class CBUNetMapper @Inject constructor() : Mapper<CBUDto, CBUEntity> {
    override fun mapToEntity(model: CBUDto): CBUEntity {
        return CBUEntity(
            id = model.id.toLong(),
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
        return CBUDto(
            id = entity.id.toInt(),
            code = entity.code,
            currencyCode = entity.currencyCode,
            nominal = entity.nominal,
            rate = entity.rate.toNumber().toSom(),
            difference = entity.difference,
            date = entity.date,
            currencyNameUZ = entity.currencyNameUZ,
            currencyNameUZC = entity.currencyNameUZC,
            currencyNameEN = entity.currencyNameEN,
            currencyNameRU = entity.currencyNameRU
        )
    }
}