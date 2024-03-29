package uz.suxa.metaworship.presentation.fragment

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.children
import androidx.core.view.get
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uz.suxa.metaworship.R
import uz.suxa.metaworship.databinding.FragmentHomeBinding
import uz.suxa.metaworship.domain.model.SongModel
import uz.suxa.metaworship.domain.model.Tonality
import uz.suxa.metaworship.presentation.adapter.SongAdapter
import uz.suxa.metaworship.presentation.adapter.vocalist.VocalistAdapter
import uz.suxa.metaworship.presentation.viewmodel.HomeViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var songAdapter: SongAdapter
    private lateinit var searchAdapter: SongAdapter
    private lateinit var vocalistAdapter: VocalistAdapter
    private val viewModel: HomeViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[HomeViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        viewModel.syncCloud()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupFab()
        setupRecyclerView()
        observeViewModel()
    }

    override fun onResume() {
        super.onResume()
        binding.navView.menu[0].subMenu?.children?.forEach { it.isChecked = false }
        binding.navView.menu[0].subMenu?.findItem(R.id.drawerHome)?.isChecked = true
    }

    private fun setupToolbar() {
        binding.searchBar.inflateMenu(R.menu.menu_main)

        binding.searchBar.setNavigationOnClickListener {
            binding.drawerLayout.open()
        }

        binding.searchView.editText.setOnEditorActionListener { _, _, _ ->
            binding.searchView.hide()
            return@setOnEditorActionListener false
        }

        binding.searchView.editText.addTextChangedListener {
            if (it.toString().isBlank()) {
                binding.searchRV.visibility = View.GONE
            } else {
                viewModel.getSongsByQuery(it.toString())
                binding.searchRV.visibility = View.VISIBLE
            }
        }


        binding.navView.setNavigationItemSelectedListener {
            binding.navView.menu[0].subMenu?.findItem(R.id.drawerHome)?.isChecked = false
            when (it.itemId) {
                R.id.drawerHome -> {
                    viewModel.getAllSongs()
                    binding.homeRV.adapter = songAdapter
                    it.isChecked = true
                    binding.searchBar.hint = getString(R.string.search_hint)
                    binding.drawerLayout.close()
                    true
                }

                R.id.drawerVocalists -> {
                    viewModel.getVocalists()
                    binding.homeRV.adapter = vocalistAdapter
                    it.isChecked = true
                    binding.searchBar.hint = getString(R.string.vocalists)
                    binding.drawerLayout.close()
                    true
                }

                else -> false
            }
        }

        binding.searchBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.createVocalist -> {
                    val bottomSheet = InputBottomSheet()
                    bottomSheet.show(
                        childFragmentManager,
                        InputBottomSheet.TAG
                    )
                    bottomSheet.onSave = {
                        lifecycleScope.launch {
                            viewModel.createVocalist(null, it)
                            delay(500)
                            Snackbar.make(
                                binding.root,
                                R.string.vocalist_saved,
                                Snackbar.LENGTH_SHORT
                            )
                                .setAction(R.string.snackbar_dismiss) {}
                                .setAnchorView(binding.addNewSongFab)
                                .show()
                        }
                        bottomSheet.dismiss()
                    }
                    true
                }

                else -> false
            }
        }
    }

    private fun setupFab() {
        binding.addNewSongFab.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToNewSongFragment(NewSongFragment.NEW_MODE)
            )
        }
    }

    private fun setupRecyclerView() {
        val homeRV = binding.homeRV
        binding.swipeHomeRV.setOnRefreshListener {
            viewModel.syncCloud()
        }
        songAdapter = SongAdapter(childFragmentManager)
        searchAdapter = SongAdapter(childFragmentManager)
        vocalistAdapter = VocalistAdapter()
        binding.searchRV.adapter = searchAdapter
        homeRV.adapter = songAdapter
        songAdapter.onSongItemClickListener = {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToSongFragment(it)
            )
        }
        songAdapter.onSongItemEdit = {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToNewSongFragment(it)
            )
        }
        songAdapter.onSongItemEditTonality = {songId, tonality ->
            viewModel.editTonality(songId, tonality)
        }
        songAdapter.onSongItemCopy = { song, tonality ->
            copySong(song, tonality)
        }
        songAdapter.onSongItemCopyInTonality = { song, tonality ->
            copyChords(song, tonality)
        }
        songAdapter.onSongItemCopyLyrics = {
            copyLyrics(it)
        }
        songAdapter.onSongItemDelete = {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.delete_song_confirmation_title)
                .setMessage(R.string.delete_song_confirmation_message)
                .setNegativeButton(R.string.action_cancel) { dialog, _ ->
                    dialog.cancel()
                }
                .setPositiveButton(R.string.action_delete) { dialog, _ ->
                    viewModel.deleteSong(it)
                    dialog.cancel()
                }
                .show()
        }

        searchAdapter.onSongItemClickListener = {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToSongFragment(it)
            )
        }
        searchAdapter.onSongItemEdit = {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToNewSongFragment(it)
            )
        }
        searchAdapter.onSongItemEditTonality = {songId, tonality ->
            viewModel.editTonality(songId, tonality)
        }
        searchAdapter.onSongItemCopy = { song, tonality ->
            copySong(song, tonality)
        }
        searchAdapter.onSongItemCopyInTonality = { song, tonality ->
            copyChords(song, tonality)
        }
        searchAdapter.onSongItemCopyLyrics = {
            copyLyrics(it)
        }
        searchAdapter.onSongItemDelete = {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.delete_song_confirmation_title)
                .setMessage(R.string.delete_song_confirmation_message)
                .setNegativeButton(R.string.action_cancel) { dialog, _ ->
                    dialog.cancel()
                }
                .setPositiveButton(R.string.action_delete) { dialog, _ ->
                    viewModel.deleteSong(it)
                    dialog.cancel()
                }
                .show()
        }

        vocalistAdapter.onItemClick = {
            binding.searchBar.hint = it
            viewModel.getSongsByVocalist(it)
            homeRV.adapter = songAdapter
        }
        vocalistAdapter.onItemLongClick = { vocalist ->
            val bottomSheet = SongActionsBottomSheet()
            bottomSheet.setVocalist(vocalist)
            bottomSheet.show(childFragmentManager, SongActionsBottomSheet.TAG)

            bottomSheet.onSongEdit = {
                val inputBottomSheet = InputBottomSheet()
                inputBottomSheet.getVocalist(vocalist.name)
                inputBottomSheet.show(
                    childFragmentManager,
                    InputBottomSheet.TAG
                )
                inputBottomSheet.onSave = {
                    lifecycleScope.launch {
                        viewModel.createVocalist(vocalist.id, it)
                        delay(500)
                        Snackbar.make(
                            binding.root,
                            R.string.vocalist_saved,
                            Snackbar.LENGTH_SHORT
                        )
                            .setAction(R.string.snackbar_dismiss) {}
                            .setAnchorView(binding.addNewSongFab)
                            .show()
                    }
                    inputBottomSheet.dismiss()
                    bottomSheet.dismiss()
                }
            }
            bottomSheet.onSongDelete = {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(R.string.delete_vocalist_confirmation_title)
                    .setMessage(R.string.delete_vocalist_confirmation_message)
                    .setNegativeButton(R.string.action_cancel) { dialog, _ ->
                        dialog.cancel()
                    }
                    .setPositiveButton(R.string.action_delete) { dialog, _ ->
                        viewModel.deleteVocalist(vocalist.id)
                        dialog.cancel()
                    }
                    .show()
                bottomSheet.dismiss()
            }
        }
    }

    private fun observeViewModel() {
        viewModel.songs.observe(viewLifecycleOwner) {
            songAdapter.submitList(it)
        }

        viewModel.searchSongs.observe(viewLifecycleOwner) {
            searchAdapter.submitList(it)
        }

        viewModel.vocalistsDto.observe(viewLifecycleOwner) {
            vocalistAdapter.submitList(it)
        }

        viewModel.refreshing.observe(viewLifecycleOwner) {
            binding.swipeHomeRV.isRefreshing = it
        }
    }

    private fun copySong(song: SongModel, tonality: Tonality) {
        viewModel.getSong(song.id)
        viewModel.copySong.observe(viewLifecycleOwner) {
            val clipboard =
                getSystemService(requireContext(), ClipboardManager::class.java) as ClipboardManager
            val clipData = ClipData.newPlainText(it.title, viewModel.copySong(it, tonality))
            clipboard.setPrimaryClip(clipData)
        }
    }

    private fun copyChords(song: SongModel, tonality: Tonality) {
        viewModel.getChords(song.id)
        viewModel.copy.observe(viewLifecycleOwner) {
            val clipboard =
                getSystemService(requireContext(), ClipboardManager::class.java) as ClipboardManager
            val clipData = ClipData.newPlainText(
                song.title,
                viewModel.copySongChords(song.title, tonality, it)
            )
            clipboard.setPrimaryClip(clipData)
        }
    }

    private fun copyLyrics(song: SongModel) {
        viewModel.getLyrics(song.id)
        viewModel.copy.observe(viewLifecycleOwner) {
            val clipboard =
                getSystemService(requireContext(), ClipboardManager::class.java) as ClipboardManager
            val clipData =
                ClipData.newPlainText(song.title, viewModel.copySongLyrics(song.title, it))
            clipboard.setPrimaryClip(clipData)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}