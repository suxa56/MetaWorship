package uz.suxa.metaworship.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import uz.suxa.metaworship.presentation.CreateVocalistBottomSheet
import uz.suxa.metaworship.presentation.adapter.SongAdapter
import uz.suxa.metaworship.presentation.adapter.vocalist.VocalistAdapter
import uz.suxa.metaworship.presentation.viewmodel.HomeViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var songAdapter: SongAdapter
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
        binding.searchBar.menu[0].isVisible = false

        binding.searchBar.setNavigationOnClickListener {
            binding.drawerLayout.open()
        }

        binding.searchView.editText.setOnEditorActionListener { _, _, _ ->
            binding.searchBar.text = binding.searchView.text
            binding.searchView.hide()
            return@setOnEditorActionListener false
        }

        binding.searchBar.textView.addTextChangedListener {
            viewModel.getSongsByQuery(it.toString())

            binding.searchBar.menu[0].isVisible = it.toString().isNotEmpty()

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
                    val bottomSheet = CreateVocalistBottomSheet()
                    bottomSheet.show(
                        childFragmentManager,
                        CreateVocalistBottomSheet.TAG
                    )
                    bottomSheet.onSave = {
                        lifecycleScope.launch {
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
                    }
                    true
                }

                R.id.clearText -> {
                    binding.searchBar.text = null
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
        val rvSongList = binding.homeRV
        songAdapter = SongAdapter(requireContext())
        vocalistAdapter = VocalistAdapter()
        rvSongList.adapter = songAdapter
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

        vocalistAdapter.onItemClick = {
            binding.searchBar.hint = it
            viewModel.getSongsByVocalist(it)
            rvSongList.adapter = songAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.songs.observe(viewLifecycleOwner) {
            songAdapter.submitList(it)
        }

        viewModel.vocalistsDto.observe(viewLifecycleOwner) {
            vocalistAdapter.submitList(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}