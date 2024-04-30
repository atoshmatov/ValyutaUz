package uz.toshmatov.currency.core.mapper

interface Mapper<Model, Entity> {
    fun mapToEntity(model: Model): Entity
    fun mapFromEntity(entity: Entity): Model
}