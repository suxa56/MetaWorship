package uz.suxa.metaworship.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import uz.suxa.metaworship.domain.model.Tonality

abstract class TonalityViewModel(application: Application): AndroidViewModel(application) {
    val sharpTonalities = listOf(
        Tonality.C, Tonality.C_SHARP, Tonality.D, Tonality.E,
        Tonality.F_SHARP, Tonality.G, Tonality.A, Tonality.H
    )

    val flatTonalities = listOf(
        Tonality.E_FLAT, Tonality.F,
        Tonality.A_FLAT, Tonality.H_FLAT
    )

    // TODO(): create fun's convert chords to numbers and reverse
}