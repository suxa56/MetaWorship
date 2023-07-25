package uz.suxa.metaworship.data

import uz.suxa.metaworship.data.db.VocalistDbModel
import uz.suxa.metaworship.data.db.VocalistSongDbDto
import uz.suxa.metaworship.domain.dto.VocalistSongDto
import uz.suxa.metaworship.domain.model.VocalistModel

class VocalistMapper {
    fun mapEntityToDbModel(vocalist: VocalistModel) = VocalistDbModel(
        id = vocalist.id,
        name = vocalist.name
    )

    fun mapDbModelToEntity(vocalist: VocalistDbModel) = VocalistModel(
        id = vocalist.id,
        name = vocalist.name
    )

    fun mapDbDtoToEntityDto(dto: VocalistSongDbDto) = VocalistSongDto(
        name = dto.name,
        songsCount = dto.songsCount
    )

    fun mapListDbDtoToListEntityDto(listDto: List<VocalistSongDbDto>) = listDto.map {
        mapDbDtoToEntityDto(it)
    }

    fun mapListDbModelToListEntity(vocalists: List<VocalistDbModel>) = vocalists.map {
        mapDbModelToEntity(it)
    }
}