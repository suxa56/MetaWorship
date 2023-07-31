package uz.suxa.metaworship.data

import uz.suxa.metaworship.data.db.CompositionDbModel
import uz.suxa.metaworship.domain.model.CompositionModel

class CompositionMapper {

    fun mapEntityToDbModel(composition: CompositionModel): CompositionDbModel {
        return CompositionDbModel(
            id = composition.id,
            name = composition.name,
            songId = composition.songId.joinToString(SEPARATOR),
            tonality = composition.tonality.joinToString(SEPARATOR),
            creationDate = composition.creationDate
        )
    }

    fun mapDbModelToEntity(compositionDb: CompositionDbModel): CompositionModel {
        return CompositionModel(
            id = compositionDb.id,
            name = compositionDb.name,
            songId = compositionDb.songId.split(SEPARATOR),
            tonality = compositionDb.tonality.split(SEPARATOR),
            creationDate = compositionDb.creationDate
        )
    }

    fun mapListDbToEntity(list: List<CompositionDbModel>) = list.map {
        mapDbModelToEntity(it)
    }

    companion object {
        private const val SEPARATOR = ","
    }
}