package uz.suxa.metaworship.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
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
        fillTonalityField()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun observeLiveData() {
        viewModel.song.observe(viewLifecycleOwner) {
            // Title
            binding.toolbar.title = it.title

            // Tonality
            if (it.defaultTonality == Tonality.UNDEFINED) {
                binding.chipTonality.visibility = View.GONE
                binding.songTonalityTil.visibility = View.GONE
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

                (binding.songTonalityTil.editText as? AutoCompleteTextView)?.setText(
                    viewModel.parseTonality(it.defaultTonality),
                    false
                )
            }

            // Lyrics
            if (it.lyrics.isBlank()) {
                binding.lyricsCard.visibility = View.GONE
                binding.chipLyrics.isChecked = false
                binding.chipLyrics.isEnabled = false
            } else {
                binding.lyrics.text = it.lyrics
            }
        }

        viewModel.chords.observe(viewLifecycleOwner) {
            if (it.isBlank()) {
                binding.chordsCard.visibility = View.GONE
            } else {
                binding.chords.text = it
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
                binding.songTonalityTil.visibility = View.VISIBLE
                binding.chordsCard.visibility = View.VISIBLE
            } else {
                binding.songTonalityTil.visibility = View.GONE
                binding.chordsCard.visibility = View.GONE
            }
        }

        (binding.songTonalityTil.editText as? AutoCompleteTextView)
            ?.setOnItemClickListener { _, view, _, _ ->
                val tonality = (view as TextView).text
                viewModel.transpose(tonality.toString())
            }
    }

    private fun fillTonalityField() {
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            resources.getStringArray(R.array.tonalities)
        )
        (binding.songTonalityTil.editText as? AutoCompleteTextView)?.setAdapter(adapter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}