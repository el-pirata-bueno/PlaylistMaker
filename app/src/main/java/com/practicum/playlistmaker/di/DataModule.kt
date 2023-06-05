package com.practicum.playlistmaker.di

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
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



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

    singleOf(::HistoryLocalStorage).bind<HistoryStorage>()
    singleOf(::LikesLocalStorage).bind<LikesStorage>()
    singleOf(::PlaylistsLocalStorage).bind<PlaylistsStorage>()
    singleOf(::SettingsLocalStorage).bind<SettingsStorage>()
    singleOf(::RetrofitNetworkClient).bind<NetworkClient>()
    singleOf(::PlayerExternalNavigator).bind<ExternalNavigator>()

}




