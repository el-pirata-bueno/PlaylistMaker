package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.domain.db.LikedTracksInteractor
import com.practicum.playlistmaker.domain.player.PlayerInteractor
import com.practicum.playlistmaker.domain.player.impl.LikedTracksInteractorImpl
import com.practicum.playlistmaker.domain.player.impl.PlayerInteractorImpl
import com.practicum.playlistmaker.domain.search.SearchInteractor
import com.practicum.playlistmaker.domain.search.impl.SearchInteractorImpl
import com.practicum.playlistmaker.domain.settings.SettingsInteractor
import com.practicum.playlistmaker.domain.settings.impl.SettingsInteractorImpl
import com.practicum.playlistmaker.domain.sharing.SharingInteractor
import com.practicum.playlistmaker.domain.sharing.impl.SharingInteractorImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val interactorModule = module {
    factoryOf(::PlayerInteractorImpl).bind<PlayerInteractor>()
    factoryOf(::SearchInteractorImpl).bind<SearchInteractor>()
    factoryOf(::SharingInteractorImpl).bind<SharingInteractor>()
    factoryOf(::SettingsInteractorImpl).bind<SettingsInteractor>()
    singleOf(::LikedTracksInteractorImpl).bind<LikedTracksInteractor>()
}