package uz.toshmatov.currency.data.mapper.bank

import uz.toshmatov.currency.core.mapper.Mapper
import uz.toshmatov.currency.data.local.room.entity.BanksEntity
import uz.toshmatov.currency.domain.model.ExchangeBankModel
import javax.inject.Inject

class BankDaoMapper @Inject constructor() : Mapper<ExchangeBankModel, BanksEntity> {
    override fun mapToEntity(model: ExchangeBankModel): BanksEntity {
        TODO("Not yet implemented")
    }

    override fun mapFromEntity(entity: BanksEntity): ExchangeBankModel {
        return ExchangeBankModel(
            bank = entity.bank,
            buy = entity.buy,
            sell = entity.sell,
            date = entity.date
        )
    }
}