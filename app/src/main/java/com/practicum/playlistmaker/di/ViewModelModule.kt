package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.presentation.media.MediaLikedViewModel
import com.practicum.playlistmaker.presentation.media.MediaPlaylistsViewModel
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
    viewModelOf(::MediaLikedViewModel)
    viewModelOf(::MediaPlaylistsViewModel)
    viewModel { (trackId: Long, trackName: String?, artistName: String?,
                    collectionName: String?, releaseDate: String?, trackTime: String?,
                    artworkUrl100: String?, primaryGenreName: String?, country: String?,
                    previewUrl: String?, isFavorite: Boolean, isInPlaylist: Boolean) ->
        PlayerViewModel(trackId, trackName, artistName, collectionName, releaseDate, trackTime,
            artworkUrl100, primaryGenreName, country, previewUrl, isFavorite, isInPlaylist, get()) }
}

private operator fun ParametersHolder.component12(): Boolean = get(11)


private operator fun ParametersHolder.component11(): Boolean = get(10)

private operator fun ParametersHolder.component10(): String? = get(9)

private operator fun ParametersHolder.component9(): String? = get(8)

private operator fun ParametersHolder.component8(): String? = get(7)

private operator fun ParametersHolder.component7(): String? = get(6)

private operator fun ParametersHolder.component6(): String? = get(5)
