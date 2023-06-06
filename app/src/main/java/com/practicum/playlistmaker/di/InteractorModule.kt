package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.domain.player.PlayerInteractor
import com.practicum.playlistmaker.domain.player.impl.TrackPlayerInteractor
import com.practicum.playlistmaker.domain.search.SearchInteractor
import com.practicum.playlistmaker.domain.search.impl.PlayerSearchInteractor
import com.practicum.playlistmaker.domain.settings.SettingsInteractor
import com.practicum.playlistmaker.domain.settings.impl.PlayerSettingsInteractor
import com.practicum.playlistmaker.domain.sharing.SharingInteractor
import com.practicum.playlistmaker.domain.sharing.impl.PlayerSharingInteractor
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val interactorModule = module {
    factoryOf(::TrackPlayerInteractor).bind<PlayerInteractor>()
    factoryOf(::PlayerSearchInteractor).bind<SearchInteractor>()
    factoryOf(::PlayerSharingInteractor).bind<SharingInteractor>()
    factoryOf(::PlayerSettingsInteractor).bind<SettingsInteractor>()
}