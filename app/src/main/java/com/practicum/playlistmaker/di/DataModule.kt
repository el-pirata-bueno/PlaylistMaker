package com.practicum.playlistmaker.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.practicum.playlistmaker.data.network.ITunesApiService
import com.practicum.playlistmaker.data.network.NetworkClient
import com.practicum.playlistmaker.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.data.sharing.ExternalNavigator
import com.practicum.playlistmaker.data.sharing.impl.PlayerExternalNavigator
import com.practicum.playlistmaker.data.storage.impl.HistoryLocalStorage
import com.practicum.playlistmaker.data.storage.impl.LikesLocalStorage
import com.practicum.playlistmaker.data.storage.impl.PlaylistsLocalStorage
import com.practicum.playlistmaker.data.storage.impl.SettingsLocalStorage
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val HISTORY_TRACKS = "HISTORY_TRACKS"
private const val APP_SETTINGS = "APP_SETTINGS"
private const val LIKED_TRACKS = "LIKED_TRACKS"
private const val PLAYLISTS = "PLAYLISTS"

val dataModule = module {
    single<ITunesApiService> {
        val logging = HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }
        val okHttpClient = OkHttpClient
            .Builder()
            .addInterceptor(logging)
            .build()

        Retrofit.Builder()
            .baseUrl("http://itunes.apple.com/")   //можно заменить на http, чтобы работало бодрее
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build().create(ITunesApiService::class.java)
    }

    single(qualifier = named("historyPrefs")) { provideHistoryPreferences(androidApplication(), HISTORY_TRACKS) }
    single(qualifier = named("likesPrefs")) { provideLikesPreferences(androidApplication(), LIKED_TRACKS)}
    single(qualifier = named("playlistsPrefs")) { providePlaylistsPreferences(androidApplication(), PLAYLISTS) }
    single(qualifier = named("settingsPrefs")) { provideSettingsPreferences(androidApplication(), APP_SETTINGS) }

    single { HistoryLocalStorage(get(named("historyPrefs"))) }
    single { LikesLocalStorage(get(named("likesPrefs"))) }
    single { PlaylistsLocalStorage(get(named("playlistsPrefs"))) }
    single { SettingsLocalStorage(get(named("settingsPrefs"))) }

    singleOf(::RetrofitNetworkClient).bind<NetworkClient>()
    singleOf(::PlayerExternalNavigator).bind<ExternalNavigator>()

}

private fun provideHistoryPreferences(app: Application, key: String): SharedPreferences = app.getSharedPreferences(key, Context.MODE_PRIVATE)
private fun provideLikesPreferences(app: Application, key: String): SharedPreferences = app.getSharedPreferences(key, Context.MODE_PRIVATE)
private fun providePlaylistsPreferences(app: Application, key: String): SharedPreferences = app.getSharedPreferences(key, Context.MODE_PRIVATE)
private fun provideSettingsPreferences(app: Application, key: String): SharedPreferences = app.getSharedPreferences(key, Context.MODE_PRIVATE)



