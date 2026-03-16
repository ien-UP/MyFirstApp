package ru.bukivadis.myfirstapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.bukivadis.myfirstapp.R
import ru.bukivadis.myfirstapp.databinding.FragmentNewPostBinding
import ru.bukivadis.myfirstapp.viewmodel.PostViewModel

class NewPostFragment : Fragment() {

    private var _binding: FragmentNewPostBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PostViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.draft.observe(viewLifecycleOwner) { draftText ->
            if (draftText.isNotBlank()) {
                binding.edit.setText(draftText)
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            val text = binding.edit.text.toString()
            if (text.isNotBlank()) {
                viewModel.saveDraft(text)
            }
            findNavController().popBackStack()
        }

        viewModel.edited.observe(viewLifecycleOwner) { post ->
            if (post.id != 0L) {
                binding.edit.setText(post.content)
            }

        }

        // Получаем текст для редактирования из аргументов
        val existingText = arguments?.getString("postContent")
        if (!existingText.isNullOrBlank()) {
            binding.edit.setText(existingText)
        }

        binding.ok.setOnClickListener {
            val text = binding.edit.text.toString()
            if (text.isBlank()) {
                Toast.makeText(requireContext(), R.string.error_empty_content, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.changeContent(text)
            viewModel.save()
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
