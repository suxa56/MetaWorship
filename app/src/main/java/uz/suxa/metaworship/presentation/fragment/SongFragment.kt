package uz.suxa.metaworship.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import uz.suxa.metaworship.R
import uz.suxa.metaworship.databinding.FragmentSongBinding
import uz.suxa.metaworship.domain.model.Tonality
import uz.suxa.metaworship.presentation.viewmodel.SongViewModel

class SongFragment : Fragment() {

    private val args by navArgs<SongFragmentArgs>()

    private var _binding: FragmentSongBinding? = null
    private val binding get() = _binding!!

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[SongViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSongBinding.inflate(inflater, container, false)
        viewModel.getSong(args.songId)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupToolbar()
        observeLiveData()
        setupClickListener()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun observeLiveData() {
        viewModel.song.observe(viewLifecycleOwner) {
            binding.toolbar.title = it.title

            if (it.defaultTonality == Tonality.UNDEFINED) {
                binding.chipTonality.visibility = View.GONE
                binding.chipGroupSongTonalities.visibility = View.GONE
                binding.chipChords.isChecked = false
                binding.chipChords.isEnabled = false
            } else {
                binding.chipTonality.text = String.format(
                    getString(
                        R.string.chip_tonality,
                        it.defaultTonality.toString()
                            .replace("_FLAT", "b")
                            .replace("_SHARP", "#")
                    )
                )
            }

            when (it.defaultTonality) {
                Tonality.C -> {
                    binding.chipTonalityC.isChecked = true
                }

                Tonality.C_SHARP -> {
                    binding.chipTonalityCSharp.isChecked = true
                }

                Tonality.D -> {
                    binding.chipTonalityD.isChecked = true
                }

                Tonality.E_FLAT -> {
                    binding.chipTonalityEFlat.isChecked = true
                }

                Tonality.E -> {
                    binding.chipTonalityE.isChecked = true
                }

                Tonality.F -> {
                    binding.chipTonalityF.isChecked = true
                }

                Tonality.F_SHARP -> {
                    binding.chipTonalityFSharp.isChecked = true
                }

                Tonality.G -> {
                    binding.chipTonalityG.isChecked = true
                }

                Tonality.A_FLAT -> {
                    binding.chipTonalityAFlat.isChecked = true
                }

                Tonality.A -> {
                    binding.chipTonalityA.isChecked = true
                }

                Tonality.H_FLAT -> {
                    binding.chipTonalityHFlat.isChecked = true
                }

                Tonality.H -> {
                    binding.chipTonalityH.isChecked = true
                }

                else -> {}
            }

            if (it.lyrics.isBlank()) {
                binding.lyricsCard.visibility = View.GONE
                binding.chipLyrics.isChecked = false
                binding.chipLyrics.isEnabled = false
            } else {
                binding.lyrics.text = it.lyrics
            }
        }
    }

    private fun setupClickListener() {
        binding.chipLyrics.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.lyricsCard.visibility = View.VISIBLE
            } else {
                binding.lyricsCard.visibility = View.GONE
            }
        }

        binding.chipChords.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.chipGroupSongTonalities.visibility = View.VISIBLE
                binding.chordsCard.visibility = View.VISIBLE
            } else {
                binding.chipGroupSongTonalities.visibility = View.GONE
                binding.chordsCard.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}