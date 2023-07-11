package uz.suxa.metaworship.data

import uz.suxa.metaworship.data.db.VocalistDbModel
import uz.suxa.metaworship.domain.model.VocalistModel

class VocalistMapper {
    fun mapEntityToDbModel(vocalist: VocalistModel) = VocalistDbModel(
        id = vocalist.id,
        name = vocalist.name
    )

    fun mapDbModelToEntity(vocalist: VocalistDbModel) = VocalistDbModel(
        id = vocalist.id,
        name = vocalist.name
    )

    fun mapListDbModelToListEntity(vocalists: List<VocalistDbModel>) = vocalists.map {
        mapDbModelToEntity(it)
    }
}