package uz.suxa.metaworship.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uz.suxa.metaworship.R
import uz.suxa.metaworship.databinding.FragmentVocalistsBinding
import uz.suxa.metaworship.presentation.CreateVocalistBottomSheet
import uz.suxa.metaworship.presentation.adapter.vocalist.VocalistAdapter
import uz.suxa.metaworship.presentation.viewmodel.VocalistViewModel

class VocalistsFragment : Fragment() {

    private var _binding: FragmentVocalistsBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: VocalistAdapter
    private val viewModel: VocalistViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[VocalistViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVocalistsBinding.inflate(inflater, container, false)
        viewModel.getVocalists()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupToolbar()
        setupRecyclerView()
        observeViewModel()
    }

    private fun setupToolbar() {
        binding.toolbar.title = findNavController().currentDestination?.label
        binding.toolbar.inflateMenu(R.menu.menu_main)

        binding.toolbar.setNavigationOnClickListener {
            binding.drawerLayout.open()
        }

        binding.navView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.drawerHome -> {
                    findNavController().navigate(R.id.action_VocalistsFragment_to_HomeFragment)
                    binding.drawerLayout.close()
                    true
                }
                else -> false
            }
        }

        binding.navView.menu[0].subMenu?.findItem(R.id.drawerVocalists)?.isChecked = true

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
                            Snackbar.make(
                                binding.root,
                                R.string.vocalist_saved,
                                Snackbar.LENGTH_SHORT
                            )
                                .setAction(R.string.snackbar_dismiss) {}
                                .show()
                        }
                    }
                    true
                }

                else -> false
            }
        }
    }

    private fun setupRecyclerView() {
        val rvVocalistList = binding.rvVocalistList
        adapter = VocalistAdapter(requireContext())
        rvVocalistList.adapter = adapter
        adapter.onItemClickListener = {
            // TODO(): Move to fragment with all songs with selected vocalist
        }
    }

    private fun observeViewModel() {
        viewModel.vocalistsDto.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}