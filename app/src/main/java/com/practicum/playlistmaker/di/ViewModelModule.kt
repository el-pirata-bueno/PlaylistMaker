package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.ui.player.view_model.PlayerViewModel
import com.practicum.playlistmaker.ui.search.view_model.SearchViewModel
import com.practicum.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::SearchViewModel)
    viewModelOf(::SettingsViewModel)
    viewModel { (trackId: Int) -> PlayerViewModel(trackId, get(), get()) }
}