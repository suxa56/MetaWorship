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
                binding.chipChords.visibility = View.GONE
            } else {
                binding.chipTonality.text = String.format(
                    getString(R.string.chip_tonality, it.defaultTonality)
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}