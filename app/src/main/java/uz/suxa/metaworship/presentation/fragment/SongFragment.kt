package uz.suxa.metaworship.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import kotlinx.coroutines.launch
import uz.suxa.metaworship.databinding.FragmentSongBinding
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observeLiveData()
    }

    private fun observeLiveData() {
        lifecycleScope.launch {
            viewModel.getSong(args.songId).observe(viewLifecycleOwner) { song ->
                binding.lol.text = song.toString()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}