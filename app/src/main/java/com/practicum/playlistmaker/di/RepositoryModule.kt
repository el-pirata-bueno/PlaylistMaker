package com.practicum.playlistmaker.di

import android.media.MediaPlayer
import com.practicum.playlistmaker.data.search.LikedTracksIdsRepositoryImpl
import com.practicum.playlistmaker.data.LikedTracksRepositoryImpl
import com.practicum.playlistmaker.data.converters.TrackDbConvertor
import com.practicum.playlistmaker.data.player.TrackPlayerImpl
import com.practicum.playlistmaker.data.search.SearchHistoryImpl
import com.practicum.playlistmaker.data.search.SearchRepositoryImpl
import com.practicum.playlistmaker.data.settings.SettingsRepository
import com.practicum.playlistmaker.data.settings.impl.SettingsRepositoryImpl
import com.practicum.playlistmaker.domain.db.LikedTracksIdsRepository
import com.practicum.playlistmaker.domain.db.LikedTracksRepository
import com.practicum.playlistmaker.domain.player.TrackPlayer
import com.practicum.playlistmaker.domain.search.SearchHistory
import com.practicum.playlistmaker.domain.search.SearchRepository
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryModule = module {
    factoryOf(::TrackPlayerImpl).bind<TrackPlayer>()
    factoryOf(::SettingsRepositoryImpl).bind<SettingsRepository>()
    factoryOf(::SearchHistoryImpl).bind<SearchHistory>()
    factoryOf(::SearchRepositoryImpl).bind<SearchRepository>()
    factoryOf(::LikedTracksRepositoryImpl).bind<LikedTracksRepository>()
    factoryOf(::LikedTracksIdsRepositoryImpl).bind<LikedTracksIdsRepository>()
    factory { MediaPlayer() }
    factory { TrackDbConvertor() }
}