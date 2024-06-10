package uz.toshmatov.currency.data.mapper.nbu

import uz.toshmatov.currency.core.mapper.Mapper
import uz.toshmatov.currency.data.remote.model.NBUDto
import uz.toshmatov.currency.domain.model.NBUModel
import javax.inject.Inject

class NBUMapper @Inject constructor() : Mapper<NBUModel, NBUDto> {
    override fun mapToEntity(model: NBUModel): NBUDto {
        TODO("Not yet implemented")
    }

    override fun mapFromEntity(entity: NBUDto): NBUModel {
        return NBUModel(
            title = entity.title,
            code = entity.code,
            cbPrice = entity.cbPrice,
            nbuBuyPrice = entity.nbuBuyPrice.orEmpty(),
            nbuCellPrice = entity.nbuCellPrice.orEmpty(),
            date = entity.date,
        )
    }
}