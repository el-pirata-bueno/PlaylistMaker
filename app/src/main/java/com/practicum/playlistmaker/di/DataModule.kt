package com.practicum.playlistmaker.di

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.data.network.ITunesApiService
import com.practicum.playlistmaker.data.network.NetworkClient
import com.practicum.playlistmaker.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.data.sharing.ExternalNavigator
import com.practicum.playlistmaker.data.sharing.impl.PlayerExternalNavigator
import com.practicum.playlistmaker.data.storage.HistoryStorage
import com.practicum.playlistmaker.data.storage.LikesStorage
import com.practicum.playlistmaker.data.storage.PlaylistsStorage
import com.practicum.playlistmaker.data.storage.SettingsStorage
import com.practicum.playlistmaker.data.storage.impl.HistoryLocalStorage
import com.practicum.playlistmaker.data.storage.impl.LikesLocalStorage
import com.practicum.playlistmaker.data.storage.impl.PlaylistsLocalStorage
import com.practicum.playlistmaker.data.storage.impl.SettingsLocalStorage
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
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

    //single<HistoryStorage> {
    //    HistoryLocalStorage(provideSharedPreference(androidContext(), HISTORY_TRACKS))
    //}

    //single<LikesStorage> {
    //    LikesLocalStorage(provideSharedPreference(androidContext(), LIKED_TRACKS))
    //}

    //single<PlaylistsStorage> {
    //    PlaylistsLocalStorage(provideSharedPreference(androidContext(), PLAYLISTS))
    //}

    //single<SettingsStorage> {
    //    SettingsLocalStorage(provideSharedPreference(androidContext(), APP_SETTINGS))
    //}

    //(trackId: Int) -> PlayerViewModel(trackId, get(), get()) }
    singleOf(::HistoryLocalStorage).bind<HistoryStorage>()
    singleOf(::LikesLocalStorage).bind<LikesStorage>()
    singleOf(::PlaylistsLocalStorage).bind<PlaylistsStorage>()
    singleOf(::SettingsLocalStorage).bind<SettingsStorage>()
    singleOf(::RetrofitNetworkClient).bind<NetworkClient>()
    singleOf(::PlayerExternalNavigator).bind<ExternalNavigator>()

    //single { provideSharedPreference(androidContext(), APP_SETTINGS) : SharedPreferences -> SettingsLocalStorage(sharedPreferences) }

    //single { provideSharedPreference(androidContext(), HISTORY_TRACKS) }
    //single { provideSharedPreference(androidContext(), LIKED_TRACKS) }
    //single { provideSharedPreference(androidContext(), PLAYLISTS) }
    single { provideSharedPreference(androidContext(), APP_SETTINGS) }
}

private fun provideSharedPreference(context: Context, sharedPreferencesKey: String): SharedPreferences =
    context.getSharedPreferences(sharedPreferencesKey, AppCompatActivity.MODE_PRIVATE)