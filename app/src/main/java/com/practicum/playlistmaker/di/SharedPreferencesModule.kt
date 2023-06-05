package com.practicum.playlistmaker.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import org.koin.android.ext.koin.androidApplication
import org.koin.core.qualifier.named
import org.koin.dsl.module

private const val HISTORY_TRACKS = "HISTORY_TRACKS"
private const val APP_SETTINGS = "APP_SETTINGS"
private const val LIKED_TRACKS = "LIKED_TRACKS"
private const val PLAYLISTS = "PLAYLISTS"

val sharedPreferencesModule = module {
    single(named("historyPrefs")) { provideHistoryPreferences(androidApplication(), HISTORY_TRACKS) }
    single(named("likesPrefs")) { provideLikesPreferences(androidApplication(), LIKED_TRACKS)}
    single(named("playlistsPrefs")) { providePlaylistsPreferences(androidApplication(), PLAYLISTS) }
    single(named("settingsPrefs")) { provideSettingsPreferences(androidApplication(), APP_SETTINGS) }
}

private fun provideHistoryPreferences(app: Application, key: String): SharedPreferences = app.getSharedPreferences(key, Context.MODE_PRIVATE)
private fun provideLikesPreferences(app: Application, key: String): SharedPreferences = app.getSharedPreferences(key, Context.MODE_PRIVATE)
private fun providePlaylistsPreferences(app: Application, key: String): SharedPreferences = app.getSharedPreferences(key, Context.MODE_PRIVATE)
private fun provideSettingsPreferences(app: Application, key: String): SharedPreferences = app.getSharedPreferences(key, Context.MODE_PRIVATE)