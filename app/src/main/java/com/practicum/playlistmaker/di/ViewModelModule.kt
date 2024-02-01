package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.presentation.media.favorites.MediaLikedTracksViewModel
import com.practicum.playlistmaker.presentation.media.playlists.MediaEditPlaylistViewModel
import com.practicum.playlistmaker.presentation.media.playlists.MediaNewPlaylistViewModel
import com.practicum.playlistmaker.presentation.media.playlists.MediaPlaylistPageViewModel
import com.practicum.playlistmaker.presentation.media.playlists.MediaPlaylistsViewModel
import com.practicum.playlistmaker.presentation.player.PlayerViewModel
import com.practicum.playlistmaker.presentation.search.SearchViewModel
import com.practicum.playlistmaker.presentation.settings.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.parameter.ParametersHolder
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::SearchViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::MediaLikedTracksViewModel)
    viewModelOf(::MediaPlaylistsViewModel)
    viewModel { (playlistId: Int?) -> MediaEditPlaylistViewModel(playlistId, get()) }
    viewModel { (playlistId: Int?) -> MediaNewPlaylistViewModel(playlistId, get()) }
    viewModel { (playlistId: Int) -> MediaPlaylistPageViewModel(playlistId, get(), get(), get()) }
    viewModel { (trackId: Long, trackName: String?, artistName: String?,
                    collectionName: String?, releaseDate: String?, trackTimeMillis: Int?,
                    artworkUrl100: String?, primaryGenreName: String?, country: String?,
                    previewUrl: String?, isFavorite: Boolean) ->
        PlayerViewModel(trackId, trackName, artistName, collectionName, releaseDate, trackTimeMillis,
            artworkUrl100, primaryGenreName, country, previewUrl, isFavorite, get(), get(), get()) }
}


private operator fun ParametersHolder.component12(): Boolean = get(11)


private operator fun ParametersHolder.component11(): Boolean = get(10)

private operator fun ParametersHolder.component10(): String? = get(9)

private operator fun ParametersHolder.component9(): String? = get(8)

private operator fun ParametersHolder.component8(): String? = get(7)

private operator fun ParametersHolder.component7(): String? = get(6)

private operator fun ParametersHolder.component6(): Int? = get(5)
