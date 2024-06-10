package uz.toshmatov.currency.data.mapper.nbu

import uz.toshmatov.currency.core.mapper.Mapper
import uz.toshmatov.currency.data.local.room.entity.NBUEntity
import uz.toshmatov.currency.domain.model.NBUModel
import javax.inject.Inject

class NBUDaoMapper @Inject constructor() : Mapper<NBUModel, NBUEntity> {
    override fun mapToEntity(model: NBUModel): NBUEntity {
        TODO("Not yet implemented")
    }

    override fun mapFromEntity(entity: NBUEntity): NBUModel {
        return NBUModel(
            id = entity.id,
            title = entity.title,
            code = entity.code,
            cbPrice = entity.cbPrice,
            nbuBuyPrice = entity.nbuBuyPrice,
            nbuCellPrice = entity.nbuCellPrice,
            date = entity.date
        )
    }
}