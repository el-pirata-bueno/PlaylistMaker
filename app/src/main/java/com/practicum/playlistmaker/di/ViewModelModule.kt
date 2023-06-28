package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.presentation.media.TrackCollectionsVIewModel
import com.practicum.playlistmaker.presentation.player.PlayerViewModel
import com.practicum.playlistmaker.presentation.search.SearchViewModel
import com.practicum.playlistmaker.presentation.settings.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::SearchViewModel)
    viewModelOf(::SettingsViewModel)
    viewModel { TrackCollectionsVIewModel() }
    viewModel { (trackId: Int) -> PlayerViewModel(trackId, get()) }
}