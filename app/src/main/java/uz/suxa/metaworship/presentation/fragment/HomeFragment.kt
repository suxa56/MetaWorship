package uz.suxa.metaworship.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import uz.suxa.metaworship.presentation.viewmodel.HomeViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: SongAdapter
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

    private fun setupToolbar() {
        binding.toolbar.title = findNavController().currentDestination?.label
        binding.toolbar.inflateMenu(R.menu.menu_main)

        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.createVocalist -> {
                    val bottomSheet = CreateVocalistBottomSheet()
                    bottomSheet.show(
                        childFragmentManager,
                        CreateVocalistBottomSheet.TAG
                    )
                    bottomSheet.onSave = {
                        lifecycleScope.launch {
                            delay(50)
                            Snackbar.make(binding.root, R.string.vocalist_saved, Snackbar.LENGTH_SHORT)
                                .setAction(R.string.snackbar_dismiss) {}
                                .setAnchorView(binding.addNewSongFab)
                                .show()
                        }
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
        val rvSongList = binding.rvSongList
        adapter = SongAdapter(requireContext())
        rvSongList.adapter = adapter
        adapter.onSongItemClickListener = {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToSongFragment(it)
            )
        }
        adapter.onSongItemEdit = {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToNewSongFragment(it)
            )
        }
        adapter.onSongItemDelete = {
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
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.getSongs().observe(viewLifecycleOwner) { list ->
                adapter.submitList(list)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}