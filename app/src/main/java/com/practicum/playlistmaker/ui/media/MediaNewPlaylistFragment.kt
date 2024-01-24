package com.practicum.playlistmaker.ui.media

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.practicum.playlistmaker.presentation.media.MediaNewPlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MediaNewPlaylistFragment : Fragment()  {

    private val viewModel by viewModel<MediaNewPlaylistViewModel>()

    private var _binding: FragmentNewPlaylistBinding? = null
    private val binding get() = _binding!!

    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>
    private var coverUri: Uri? = null
    private var coverFilePath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    dialogBuilder()
                }
            }
            )
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewPlaylistBinding.inflate(inflater, container, false)

        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                coverUri = uri
                binding.playlistCover.setImageURI(uri)
                binding.playlistCover.tag = "coverIsChosen"
            } else {
                Log.d("PhotoPicker", "No media selected")
            }

        }

    return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



    private fun initListeners() {

        binding.createPlaylistButton.isEnabled = binding.inputTitleEditText.text.toString().isNotEmpty()
        binding.createPlaylistButton.isClickable = binding.inputTitleEditText.text.toString().isNotEmpty()

        binding.arrowBackButton.setOnClickListener {
            dialogBuilder()
        }
        binding.playlistCover.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        binding.createPlaylistButton.setOnClickListener {
            if (coverUri != null) {
                coverFilePath = saveImageToPrivateStorage(coverUri!!, binding.inputTitleEditText.text.toString())
            }
            viewModel.createPlaylist(
                binding.inputTitleEditText.text.toString(),
                binding.inputDescriptionEditText.text.toString(),
                coverFilePath
            )

            findNavController().popBackStack()
            val toastText = getString(R.string.any_playlist_created, binding.inputTitleEditText.text.toString())
            Toast.makeText(requireContext(), toastText, Toast.LENGTH_LONG).show()
        }

        binding.inputTitleEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                buttonCreateCheckActive()
            }
            override fun afterTextChanged(s: Editable?) {
                //buttonCreateCheckActive()
            }
        }
        )

        binding.inputDescriptionEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {}
        }
        )

        binding.inputDescriptionEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                hideKeyboard()
            }
            false
        }
    }

    private fun buttonCreateCheckActive() {
        binding.createPlaylistButton.isEnabled = binding.inputTitleEditText.text.toString().isNotEmpty()
        binding.createPlaylistButton.isClickable = binding.inputTitleEditText.text.toString().isNotEmpty()
    }

    private fun dialogBuilder() {
        val title = binding.inputTitleEditText.text?.toString() ?: ""
        val description = binding.inputDescriptionEditText.text?.toString() ?: ""
        val coverTag = binding.playlistCover.tag?.toString() ?: ""

        if (title.isNotEmpty() || description.isNotEmpty() || coverTag.isNotEmpty()
        ) {
            MaterialAlertDialogBuilder(requireContext(), R.style.Theme_MyApp_Dialog_Alert)
                .setTitle("Завершить создание плейлиста?")
                .setMessage("Все несохраненные данные будут потеряны")
                .setNeutralButton("Отмена") { dialog, which ->

                }
                .setPositiveButton("Завершить") { dialog, which ->
                    findNavController().popBackStack()
                }
                .show()
        }
        else {
            findNavController().popBackStack()
        }
    }

    private fun saveImageToPrivateStorage(uri: Uri, title: String): String {
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
        val filename = title + LocalDateTime.now().format(formatter)

        val filePath = File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "Playlist Maker")
        if (!filePath.exists()){
            filePath.mkdirs()
        }

        val file = File(filePath, filename)
        val inputStream = requireActivity().contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)

        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 50, outputStream)

        return file.path
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(binding.inputDescriptionEditText.windowToken, 0)
    }

    companion object {
        fun newInstance() = MediaNewPlaylistFragment().apply { }
    }
}