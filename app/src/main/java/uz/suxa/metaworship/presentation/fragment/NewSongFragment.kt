package uz.suxa.metaworship.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.core.view.children
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputLayout
import uz.suxa.metaworship.R
import uz.suxa.metaworship.databinding.FragmentNewSongBinding
import uz.suxa.metaworship.presentation.viewmodel.AddSongViewModel


class NewSongFragment : Fragment() {

    private var _binding: FragmentNewSongBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddSongViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[AddSongViewModel::class.java]
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

        binding.toolbar.title = findNavController().currentDestination?.label
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }


        fillTonalityField()
        observeLiveData()
        setListener()
    }

    private fun fillTonalityField() {
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            resources.getStringArray(R.array.tonalities)
        )
        (binding.songTonalityTil.editText as? AutoCompleteTextView)?.setAdapter(adapter)
    }

    private fun observeLiveData() {
        viewModel.titleError.observe(viewLifecycleOwner) {
            if (it) binding.songTitleTil.error = getString(R.string.title_error)
        }
        viewModel.tonalityError.observe(viewLifecycleOwner) {
            if (it) binding.songTonalityTil.error = getString(R.string.tonality_error)
        }
        viewModel.chordsError.observe(viewLifecycleOwner) {
            if (it) binding.songChordsTil.error = getString(R.string.chords_error)
        }

        viewModel.modulationError.observe(viewLifecycleOwner) { list ->
            list.forEach { index ->
                binding.modulationContainer.getChildAt(index)
                    .findViewById<TextInputLayout>(R.id.modulation).error =
                    getString(R.string.tonality_error)
            }
        }
        viewModel.vocalistError.observe(viewLifecycleOwner) { list ->
            list.forEach { index ->
                binding.vocalistContainer.getChildAt(index)
                    .findViewById<TextInputLayout>(R.id.vocalist).error =
                    getString(R.string.vocalist_error)
            }
        }
        viewModel.vocalistTonalityError.observe(viewLifecycleOwner) { list ->
            list.forEach { index ->
                binding.vocalistContainer.getChildAt(index)
                    .findViewById<TextInputLayout>(R.id.tonality).error =
                    getString(R.string.tonality_error)
            }
        }
        viewModel.soloPartError.observe(viewLifecycleOwner) { list ->
            list.forEach { index ->
                binding.soloContainer.getChildAt(index)
                    .findViewById<TextInputLayout>(R.id.part).error =
                    getString(R.string.solo_part_error)
            }
        }
        viewModel.soloError.observe(viewLifecycleOwner) { list ->
            list.forEach { index ->
                binding.soloContainer.getChildAt(index)
                    .findViewById<TextInputLayout>(R.id.soloTil).error =
                    getString(R.string.solo_error)
            }
        }
    }

    private fun setListener() {
        binding.saveSongBtn.setOnClickListener {
            val vocalists = binding.vocalistContainer.children.map { x ->
                x.findViewById<TextInputLayout>(R.id.vocalist).editText?.text.toString()
            }.toList().toMutableList()
            val tonalities = binding.vocalistContainer.children.map { x ->
                x.findViewById<TextInputLayout>(R.id.tonality).editText?.text.toString()
            }.toList().toMutableList()
            val modulations = binding.modulationContainer.children.map { x ->
                x.findViewById<TextInputLayout>(R.id.modulation).editText?.text.toString()
            }.toList().toMutableList()
            val soloParts = binding.soloContainer.children.map { x ->
                x.findViewById<TextInputLayout>(R.id.part).editText?.text.toString()
            }.toList().toMutableList()
            val solos = binding.soloContainer.children.map { x ->
                x.findViewById<TextInputLayout>(R.id.soloTil).editText?.text.toString()
            }.toList().toMutableList()
            viewModel.addSong(
                title = binding.songTitleTil.editText?.text.toString(),
                lyrics = binding.songLyricsTil.editText?.text.toString(),
                chords = binding.songChordsTil.editText?.text.toString(),
                tonalityString = binding.songTonalityTil.editText?.text.toString(),
                modulations = modulations,
                vocalists = vocalists,
                tonalities = tonalities,
                soloParts = soloParts,
                solos = solos,
                tempo = binding.songTempoTil.editText?.text.toString(),
                shouldClose = object : AddSongViewModel.ShouldClose {
                    override fun onComplete() {
                        findNavController().popBackStack()
                    }
                }
            )
        }

        // clear error on field change
        binding.songTitleTil.editText?.addTextChangedListener {
            binding.songTitleTil.error = null
        }
        binding.songTonalityTil.editText?.addTextChangedListener {
            binding.songTonalityTil.error = null
            binding.cleatTonality.visibility = View.VISIBLE
        }
        binding.songChordsTil.editText?.addTextChangedListener {
            binding.songChordsTil.error = null
        }
        binding.cleatTonality.setOnClickListener {
            binding.songTonalityTil.editText?.text = null
            binding.cleatTonality.visibility = View.GONE
        }

        // add fields
        binding.addVocalist.setOnClickListener {
            createVocalistField()
        }
        binding.addModulation.setOnClickListener {
            createModulationField()
        }
        binding.addSolo.setOnClickListener {
            createSoloField()
        }

    }

    private fun createModulationField() {
        val linearLayout = LinearLayout(requireContext())
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        linearLayout.layoutParams = params
        val container = binding.modulationContainer

        val view = layoutInflater.inflate(R.layout.modulation_field, null)
        view.layoutParams = params
        linearLayout.addView(view)
        container.addView(linearLayout)
        // Fill fields
        val modulationField = view.findViewById<TextInputLayout>(R.id.modulation)
        (modulationField.editText
                as? MaterialAutoCompleteTextView)?.setSimpleItems(
            resources.getStringArray(R.array.tonalities)
        )
        // Remove fields
        view.findViewById<ImageButton>(R.id.modulationRemoveBtn).setOnClickListener {
            container.removeView(linearLayout)
        }
        // Clear error on change
        modulationField.editText?.addTextChangedListener {
            modulationField.error = null
        }

    }

    private fun createVocalistField() {
        val linearLayout = LinearLayout(requireContext())
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        linearLayout.layoutParams = params
        val container = binding.vocalistContainer

        val view = layoutInflater.inflate(R.layout.vocalist_tonality_field, null)
        view.layoutParams = params
        linearLayout.addView(view)
        container.addView(linearLayout)
        // Fill fields
        val vocalistField = view.findViewById<TextInputLayout>(R.id.vocalist)
        val tonalityField = view.findViewById<TextInputLayout>(R.id.tonality)
        (vocalistField.editText
                as? MaterialAutoCompleteTextView)?.setSimpleItems(
            resources.getStringArray(R.array.vocalists)
        )
        (tonalityField.editText
                as? MaterialAutoCompleteTextView)?.setSimpleItems(
            resources.getStringArray(R.array.tonalities)
        )
        // Remove fields
        view.findViewById<ImageButton>(R.id.vocalistTonalityRemoveBtn).setOnClickListener {
            container.removeView(linearLayout)
        }
        vocalistField.editText?.addTextChangedListener {
            vocalistField.error = null
        }
        // Clear error on change
        tonalityField.editText?.addTextChangedListener {
            tonalityField.error = null
        }
    }

    private fun createSoloField() {
        val linearLayout = LinearLayout(requireContext())
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        linearLayout.layoutParams = params
        val container = binding.soloContainer

        val view = layoutInflater.inflate(R.layout.solo_field, null)
        view.layoutParams = params
        linearLayout.addView(view)
        container.addView(linearLayout)
        // Fill fields
        val partField = view.findViewById<TextInputLayout>(R.id.part)
        val soloField = view.findViewById<TextInputLayout>(R.id.soloTil)
        (partField.editText
                as? MaterialAutoCompleteTextView)?.setSimpleItems(
            resources.getStringArray(R.array.part)
        )

        // Remove fields
        view.findViewById<ImageButton>(R.id.soloPartRemoveBtn).setOnClickListener {
            container.removeView(linearLayout)
        }
        // Clear error on change
        partField.editText?.addTextChangedListener {
            partField.error = null
        }
        soloField.editText?.addTextChangedListener {
            soloField.error = null
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}