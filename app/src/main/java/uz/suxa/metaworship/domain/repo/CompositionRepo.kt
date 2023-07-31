package uz.suxa.metaworship.domain.repo

import uz.suxa.metaworship.domain.model.CompositionModel

interface CompositionRepo {
    suspend fun addComposition(composition: CompositionModel)
}