package com.practicum.playlistmaker.ui.media.playlists

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
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
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistNewEditBinding
import com.practicum.playlistmaker.domain.model.Playlist
import com.practicum.playlistmaker.presentation.media.playlists.MediaNewPlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

open class MediaNewPlaylistFragment : Fragment()  {

    open val viewModel: MediaNewPlaylistViewModel by viewModel {
        parametersOf(
            requireArguments().getInt(ARGS_PLAYLIST_ID)
        )
    }

    var _binding: FragmentPlaylistNewEditBinding? = null
    val binding get() = _binding!!

    lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>

    var coverUri: Uri? = null
    var coverFilePath: String? = null
    var currentPlaylist: Playlist? = null

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
        _binding = FragmentPlaylistNewEditBinding.inflate(inflater, container, false)

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

        viewModel.getPlaylistNewEditStateLiveData().observe(viewLifecycleOwner) {
            render(it)
        }

        buttonStatus()
        initListeners()
        initEditText()
        initButtonListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun render(state: MediaNewEditPlaylistState) {
        when (state) {
            is MediaNewEditPlaylistState.NewPlaylist -> showNewPlaylist()
            is MediaNewEditPlaylistState.EditPlaylist -> showEditPlaylist(state.playlist)
        }
    }
    open fun showNewPlaylist() {
        binding.titlePlaylist.text = getString(R.string.new_playlist)
        binding.playlistButton.text = getString(R.string.create)
    }

    open fun showEditPlaylist(playlist: Playlist?) {
        currentPlaylist = playlist
    }

    open fun initButtonListener() {
        binding.playlistButton.setOnClickListener {
            if (coverUri != null) {
                coverFilePath = saveImageToPrivateStorage(
                    coverUri!!,
                    binding.inputTitleEditText.text.toString()
                )
            }
            viewModel.createPlaylist(
                binding.inputTitleEditText.text.toString(),
                binding.inputDescriptionEditText.text.toString(),
                coverFilePath
            )

            findNavController().navigateUp()

            val toastText = getString(R.string.any_playlist_created,
                binding.inputTitleEditText.text.toString())
            Toast.makeText(requireContext(), toastText, Toast.LENGTH_LONG).show()
        }
    }

    fun saveImageToPrivateStorage(uri: Uri, title: String): String {
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

    private fun pickImage() {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }
    private fun hideKeyboard() {
        val inputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(binding.inputDescriptionEditText.windowToken, 0)
    }

    private fun initListeners() {
        binding.arrowBackButton.setOnClickListener {
            dialogBuilder()
        }

        binding.playlistCover.setOnClickListener {
            pickImage()
        }
    }

    private fun buttonCreateCheckActive(text: String) {
        binding.playlistButton.isEnabled = text.isNotEmpty()
        binding.playlistButton.isClickable = text.isNotEmpty()
    }
    private fun initEditText() {
        binding.inputTitleEditText.doOnTextChanged { text, start, before, count ->
            buttonCreateCheckActive(text.toString())
        }

        binding.inputDescriptionEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                hideKeyboard()
            }
            false
        }
    }
    private fun buttonStatus() {
        binding.playlistButton.isEnabled =
            binding.inputTitleEditText.text.toString().isNotEmpty()
        binding.playlistButton.isClickable =
            binding.inputTitleEditText.text.toString().isNotEmpty()
    }

    private fun dialogBuilder() {
        val title = binding.inputTitleEditText.text?.toString() ?: ""
        val description = binding.inputDescriptionEditText.text?.toString() ?: ""
        val coverTag = binding.playlistCover.tag?.toString() ?: ""

        if (currentPlaylist == null && (title.isNotEmpty() || description.isNotEmpty() || coverTag.isNotEmpty())
        ) {
            MaterialAlertDialogBuilder(requireContext(), R.style.Theme_MyApp_Dialog_Alert)
                .setTitle(R.string.dialog_title_new_playlist)
                .setMessage(R.string.dialog_message_new_playlist)
                .setNeutralButton(R.string.dialog_negative_button_new_playlist) { dialog, which ->

                }
                .setPositiveButton(R.string.dialog_positive_button_new_playlist) { dialog, which ->
                    findNavController().navigateUp()
                }
                .show()
        }
        else {
            findNavController().navigateUp()
        }
    }

    companion object {
        private const val ARGS_PLAYLIST_ID = "playlistId"
        fun createArgs(playlistId: Int?): Bundle? =
            bundleOf(
                ARGS_PLAYLIST_ID to playlistId
            )
    }
}