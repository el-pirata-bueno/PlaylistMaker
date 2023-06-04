package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.data.player.AudioTrackPlayer
import com.practicum.playlistmaker.data.search.PlayerSearchRepository
import com.practicum.playlistmaker.data.search.TracksSearchHistory
import com.practicum.playlistmaker.data.settings.SettingsRepository
import com.practicum.playlistmaker.data.settings.impl.PlayerSettingsRepository
import com.practicum.playlistmaker.domain.player.TrackPlayer
import com.practicum.playlistmaker.domain.search.SearchHistory
import com.practicum.playlistmaker.domain.search.SearchRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryModule = module {
    singleOf(::AudioTrackPlayer).bind<TrackPlayer>()
    singleOf(::PlayerSearchRepository).bind<SearchRepository>()
    singleOf(::PlayerSettingsRepository).bind<SettingsRepository>()
    singleOf(::TracksSearchHistory).bind<SearchHistory>()
}