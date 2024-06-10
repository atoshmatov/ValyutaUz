package uz.toshmatov.currency.data.mapper.nbu

import uz.toshmatov.currency.core.mapper.Mapper
import uz.toshmatov.currency.data.local.room.entity.NBUEntity
import uz.toshmatov.currency.data.remote.model.NBUDto
import javax.inject.Inject

class NBUNetMapper @Inject constructor() : Mapper<NBUDto, NBUEntity> {
    override fun mapToEntity(model: NBUDto): NBUEntity {
        return NBUEntity(
            title = model.title,
            code = model.code,
            cbPrice = model.cbPrice,
            nbuBuyPrice = model.nbuBuyPrice.orEmpty(),
            nbuCellPrice = model.nbuCellPrice.orEmpty(),
            date = model.date
        )
    }

    override fun mapFromEntity(entity: NBUEntity): NBUDto {
        TODO("Not yet implemented")
    }
}