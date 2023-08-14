package uz.suxa.metaworship.data

import android.app.Application
import uz.suxa.metaworship.domain.model.CompositionModel
import uz.suxa.metaworship.domain.repo.CompositionRepo

class CompositionRepoImpl(
    application: Application
) : CompositionRepo {
    override suspend fun addComposition(composition: CompositionModel) {

    }
}