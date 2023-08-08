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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import uz.suxa.metaworship.R
import uz.suxa.metaworship.databinding.FragmentSongBinding
import uz.suxa.metaworship.domain.model.Tonality
import uz.suxa.metaworship.presentation.SongActionsBottomSheet
import uz.suxa.metaworship.presentation.adapter.solo.SoloPartAdapter
import uz.suxa.metaworship.presentation.viewmodel.SongViewModel

class SongFragment : Fragment() {

    private val args by navArgs<SongFragmentArgs>()

    private var _binding: FragmentSongBinding? = null
    private val binding get() = _binding!!

    private lateinit var soloAdapter: SoloPartAdapter
    private val bottomSheet by lazy {
        SongActionsBottomSheet()
    }

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
        setupRecyclerView()
        observeLiveData()
        setupClickListener()
        fillExposedMenus()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding.toolbar.inflateMenu(R.menu.menu_song_list)

        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.showBottomSheet -> {
                    bottomSheet.show(
                        childFragmentManager,
                        SongActionsBottomSheet.TAG
                    )
                }
            }
            true
        }
    }

    private fun setupRecyclerView() {
        val rvSongList = binding.rvSolo
        soloAdapter = SoloPartAdapter()
        rvSongList.adapter = soloAdapter
    }

    private fun observeLiveData() {
        viewModel.song.observe(viewLifecycleOwner) { song ->
            // Title
            binding.toolbar.title = song.title

            // Tonality
            if (song.defaultTonality == Tonality.UNDEFINED) {
                binding.chipTonality.visibility = View.GONE
                binding.songTonalityTil.visibility = View.GONE
                binding.capoTil.visibility = View.GONE
                binding.chipChords.isChecked = false
                binding.chipChords.isEnabled = false
            } else {
                binding.chipTonality.text = String.format(
                    getString(
                        R.string.chip_tonality,
                        song.defaultTonality.toString()
                            .replace("_FLAT", "b")
                            .replace("_SHARP", "#")
                    )
                )

                (binding.songTonalityTil.editText as? AutoCompleteTextView)?.setText(
                    viewModel.convertTonalityToSymbol(song.defaultTonality),
                    false
                )
            }

            // Lyrics
            if (song.lyrics.isBlank()) {
                binding.lyricsCard.visibility = View.GONE
                binding.chipLyrics.isChecked = false
                binding.chipLyrics.isEnabled = false
            } else {
                binding.lyrics.text = song.lyrics
            }

            bottomSheet.setSong(song)

            bottomSheet.onSongCopy = {
                viewModel.copySong(song, it)
            }

            bottomSheet.onSongCopyIn = {
                viewModel.copySongChords(song, it)
            }

            bottomSheet.onSongCopyLyrics = {
                viewModel.copySongLyrics(song)
            }

            bottomSheet.onSongDelete = {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(R.string.delete_song_confirmation_title)
                    .setMessage(R.string.delete_song_confirmation_message)
                    .setNegativeButton(R.string.action_cancel) { dialog, _ ->
                        dialog.cancel()
                    }
                    .setPositiveButton(R.string.action_delete) { dialog, _ ->
                        viewModel.deleteSong(song.id)
                        dialog.cancel()
                    }
                    .show()
            }
        }

        viewModel.chords.observe(viewLifecycleOwner) {
            if (it.isBlank()) {
                binding.chordsCard.visibility = View.GONE
            } else {
                binding.chords.text = it
            }
        }

        viewModel.soloParts.observe(viewLifecycleOwner) {
            soloAdapter.submitList(it)
        }

        viewModel.vocalistTonality.observe(viewLifecycleOwner) { list ->
            val tonalities =
                viewModel.fillTonalities(resources.getStringArray(R.array.tonalities), list)

            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1,
                tonalities
            )
            (binding.songTonalityTil.editText as? AutoCompleteTextView)?.setAdapter(adapter)
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
                binding.capoTil.visibility = View.VISIBLE
                binding.chordsCard.visibility = View.VISIBLE
            } else {
                binding.songTonalityTil.visibility = View.GONE
                binding.capoTil.visibility = View.GONE
                binding.chordsCard.visibility = View.GONE
            }
        }

        (binding.songTonalityTil.editText as? AutoCompleteTextView)
            ?.setOnItemClickListener { _, view, _, _ ->
                val tilText = (view as TextView).text
                val tonality: String = if (tilText.length == 1) {
                    tilText[0].toString()
                } else {
                    tilText[0].toString() + tilText[1].toString()
                }
                viewModel.changeTonality(tonality)
            }

        (binding.capoTil.editText as? AutoCompleteTextView)
            ?.setOnItemClickListener { _, _, position, _ ->
                viewModel.changeCapo(position)
            }
    }

    private fun fillExposedMenus() {
        fillCapoField()
    }

    private fun fillCapoField() {
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            resources.getStringArray(R.array.capo)
        )
        (binding.capoTil.editText as? AutoCompleteTextView)?.setAdapter(adapter)

        (binding.capoTil.editText as? AutoCompleteTextView)?.setText(
            "0",
            false
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}