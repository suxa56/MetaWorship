package uz.suxa.metaworship.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import uz.suxa.metaworship.R
import uz.suxa.metaworship.databinding.FragmentNewSongBinding
import uz.suxa.metaworship.presentation.viewmodel.SongViewModel

class NewSongFragment : Fragment() {

    private var _binding: FragmentNewSongBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SongViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[SongViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewSongBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeLiveData()
        setListener()
    }

    private fun observeLiveData() {
        viewModel.tonalityList.observe(viewLifecycleOwner) {
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, it)
            (binding.songTonalityTil.editText as? AutoCompleteTextView)?.setAdapter(adapter)
        }

        viewModel.titleError.observe(viewLifecycleOwner) {
            if (it) binding.songTitleTil.error = getString(R.string.title_error)
        }
        viewModel.tonalityError.observe(viewLifecycleOwner) {
            if (it) binding.songTonalityTil.error = getString(R.string.tonality_error)
        }
        viewModel.chordsError.observe(viewLifecycleOwner) {
            if (it) binding.songChordsTil.error = getString(R.string.chords_error)
        }
    }

    //    TODO(): Check, when chords are exists => tonality must be too
    private fun setListener() {
        binding.saveSongBtn.setOnClickListener {
            viewModel.addSong(
                title = binding.songTitleTil.editText?.text.toString(),
                lyrics = binding.songLyricsTil.editText?.text.toString(),
                chords = binding.songChordsTil.editText?.text.toString(),
                tonalityString = binding.songTonalityTil.editText?.text.toString(),
                tempo = binding.songTempoTil.editText?.text.toString(),
                object : SongViewModel.ShouldClose {
                    override fun onComplete() {
                        findNavController().navigate(R.id.action_NewSongFragment_to_FirstFragment)
                    }
                }
            )
        }
        binding.songTitleTil.editText?.addTextChangedListener {
            binding.songTitleTil.error = null
        }
        binding.songTonalityTil.editText?.addTextChangedListener {
            binding.songTonalityTil.error = null
        }
        binding.songChordsTil.editText?.addTextChangedListener {
            binding.songChordsTil.error = null
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}