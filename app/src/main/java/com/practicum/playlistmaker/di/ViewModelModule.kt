package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.ui.medialibrary.viewmodel.TrackCollectionsVIewModel
import com.practicum.playlistmaker.ui.player.viewmodel.PlayerViewModel
import com.practicum.playlistmaker.ui.search.viewmodel.SearchViewModel
import com.practicum.playlistmaker.ui.settings.viewmodel.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::SearchViewModel)
    viewModelOf(::SettingsViewModel)
    viewModel { TrackCollectionsVIewModel() }
    viewModel { (trackId: Int) -> PlayerViewModel(trackId, get(), get()) }
}