package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.domain.media.MediaLikedTracksIdsInteractor
import com.practicum.playlistmaker.domain.media.MediaLikedTracksInteractor
import com.practicum.playlistmaker.domain.media.MediaPlaylistsInteractor
import com.practicum.playlistmaker.domain.media.impl.MediaLikedTracksIdsInteractorImpl
import com.practicum.playlistmaker.domain.media.impl.MediaLikedTracksInteractorImpl
import com.practicum.playlistmaker.domain.media.impl.MediaPlaylistsInteractorImpl
import com.practicum.playlistmaker.domain.player.PlayerInteractor
import com.practicum.playlistmaker.domain.player.impl.PlayerInteractorImpl
import com.practicum.playlistmaker.domain.search.SearchInteractor
import com.practicum.playlistmaker.domain.search.impl.SearchInteractorImpl
import com.practicum.playlistmaker.domain.settings.SettingsInteractor
import com.practicum.playlistmaker.domain.settings.impl.SettingsInteractorImpl
import com.practicum.playlistmaker.domain.sharing.SharingInteractor
import com.practicum.playlistmaker.domain.sharing.impl.SharingInteractorImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val interactorModule = module {
    factoryOf(::PlayerInteractorImpl).bind<PlayerInteractor>()
    factoryOf(::SearchInteractorImpl).bind<SearchInteractor>()
    factoryOf(::SharingInteractorImpl).bind<SharingInteractor>()
    factoryOf(::SettingsInteractorImpl).bind<SettingsInteractor>()
    factoryOf(::MediaLikedTracksInteractorImpl).bind<MediaLikedTracksInteractor>()
    factoryOf(::MediaLikedTracksIdsInteractorImpl).bind<MediaLikedTracksIdsInteractor>()
    factoryOf(::MediaPlaylistsInteractorImpl).bind<MediaPlaylistsInteractor>()
}