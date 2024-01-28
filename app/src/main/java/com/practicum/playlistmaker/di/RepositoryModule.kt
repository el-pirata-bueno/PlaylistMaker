package com.practicum.playlistmaker.di

import android.media.MediaPlayer
import com.practicum.playlistmaker.data.converters.PlaylistMapper
import com.practicum.playlistmaker.data.converters.TrackMapper
import com.practicum.playlistmaker.data.media.MediaLikedTracksIdsRepository
import com.practicum.playlistmaker.data.media.MediaLikedTracksRepository
import com.practicum.playlistmaker.data.media.MediaPlaylistsRepository
import com.practicum.playlistmaker.data.media.impl.MediaLikedTracksIdsRepositoryImpl
import com.practicum.playlistmaker.data.media.impl.MediaLikedTracksRepositoryImpl
import com.practicum.playlistmaker.data.media.impl.MediaPlaylistsRepositoryImpl
import com.practicum.playlistmaker.data.player.TrackPlayerImpl
import com.practicum.playlistmaker.data.search.SearchHistory
import com.practicum.playlistmaker.data.search.SearchRepository
import com.practicum.playlistmaker.data.search.impl.SearchHistoryImpl
import com.practicum.playlistmaker.data.search.impl.SearchRepositoryImpl
import com.practicum.playlistmaker.data.settings.SettingsRepository
import com.practicum.playlistmaker.data.settings.impl.SettingsRepositoryImpl
import com.practicum.playlistmaker.domain.player.TrackPlayer
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryModule = module {
    factoryOf(::TrackPlayerImpl).bind<TrackPlayer>()
    factoryOf(::SettingsRepositoryImpl).bind<SettingsRepository>()
    factoryOf(::SearchHistoryImpl).bind<SearchHistory>()
    factoryOf(::SearchRepositoryImpl).bind<SearchRepository>()
    factoryOf(::MediaLikedTracksRepositoryImpl).bind<MediaLikedTracksRepository>()
    factoryOf(::MediaLikedTracksIdsRepositoryImpl).bind<MediaLikedTracksIdsRepository>()
    factoryOf(::MediaPlaylistsRepositoryImpl).bind<MediaPlaylistsRepository>()
    factory { MediaPlayer() }
    factory { TrackMapper() }
    factory { PlaylistMapper(get()) }
}