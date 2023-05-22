package uz.suxa.metaworship.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import uz.suxa.metaworship.R
import uz.suxa.metaworship.databinding.FragmentNewSongBinding

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
    }

    private fun setListener() {
        binding.saveSongBtn.setOnClickListener {
            viewModel.addSong(
                title = binding.songTitleTil.editText?.text.toString(),
                lyrics = binding.songLyricsTil.editText?.text.toString(),
                chords = binding.songChordsTil.editText?.text.toString(),
                tonality = binding.songTonalityTil.editText?.text.toString(),
                tempo = binding.songTempoTil.editText?.text.toString().toInt(),
                object : SongViewModel.ShouldClose {
                    override fun onComplete() {
                        findNavController().navigate(R.id.action_NewSongFragment_to_FirstFragment)
                    }
                }
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}