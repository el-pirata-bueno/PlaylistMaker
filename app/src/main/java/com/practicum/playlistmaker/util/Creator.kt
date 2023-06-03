package com.practicum.playlistmaker.util

import android.content.Context
import com.practicum.playlistmaker.data.network.ITunesApiService
import com.practicum.playlistmaker.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.data.player.AudioTrackPlayer
import com.practicum.playlistmaker.data.storage.LikesLocalStorage
import com.practicum.playlistmaker.data.storage.PlaylistsLocalStorage
import com.practicum.playlistmaker.data.storage.HistoryLocalStorage
import com.practicum.playlistmaker.data.search.PlayerSearchRepository
import com.practicum.playlistmaker.data.search.TracksSearchHistory
import com.practicum.playlistmaker.data.storage.SettingsLocalStorage
import com.practicum.playlistmaker.data.settings.SettingsRepository
import com.practicum.playlistmaker.data.settings.impl.PlayerSettingsRepository
import com.practicum.playlistmaker.data.sharing.ExternalNavigator
import com.practicum.playlistmaker.data.sharing.impl.PlayerExternalNavigator
import com.practicum.playlistmaker.domain.player.PlayerInteractor
import com.practicum.playlistmaker.domain.player.TrackPlayer
import com.practicum.playlistmaker.domain.player.impl.TrackPlayerInteractor
import com.practicum.playlistmaker.domain.search.SearchInteractor
import com.practicum.playlistmaker.domain.search.SearchRepository
import com.practicum.playlistmaker.domain.search.impl.PlayerSearchInteractor
import com.practicum.playlistmaker.domain.settings.SettingsInteractor
import com.practicum.playlistmaker.domain.settings.impl.PlayerSettingsInteractor
import com.practicum.playlistmaker.domain.sharing.SharingInteractor
import com.practicum.playlistmaker.domain.sharing.impl.PlayerSharingInteractor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Creator {

    private fun getPlayerRepository(context: Context): TrackPlayer {
        return AudioTrackPlayer(
            RetrofitNetworkClient(context),
            LikesLocalStorage(context.getSharedPreferences("LIKED_TRACKS", Context.MODE_PRIVATE)),
            PlaylistsLocalStorage(context.getSharedPreferences("PLAYLISTS", Context.MODE_PRIVATE)),
        )
    }

    fun providePlayerInteractor(context: Context): PlayerInteractor {
        return TrackPlayerInteractor(getPlayerRepository(context))
    }

    fun provideApi(): ITunesApiService {
        val logging = HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }
        val okHttpClient = OkHttpClient.Builder().addInterceptor(logging).build()
        return Retrofit.Builder()
            .baseUrl("http://itunes.apple.com/")   //можно заменить на http, чтобы работало бодрее
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient).build().create(ITunesApiService::class.java)
    }
    //

    private fun getSearchRepository(context: Context): SearchRepository {
        return PlayerSearchRepository(
            RetrofitNetworkClient(context),
            LikesLocalStorage(context.getSharedPreferences("LIKED_TRACKS", Context.MODE_PRIVATE)),
            PlaylistsLocalStorage(context.getSharedPreferences("PLAYLISTS", Context.MODE_PRIVATE)),
        )
    }

    private fun getSearchHistory(context: Context): TracksSearchHistory {
        return TracksSearchHistory(
            HistoryLocalStorage(
                context.getSharedPreferences(
                    "HISTORY_TRACKS",
                    Context.MODE_PRIVATE
                )
            )
        )
    }

    fun provideSearchInteractor(context: Context): SearchInteractor {
        return PlayerSearchInteractor(getSearchRepository(context), getSearchHistory(context))
    }

    private fun getSharingNavigator(context: Context): ExternalNavigator {
        return PlayerExternalNavigator(context)
    }

    fun provideSharingInteractor(context: Context): SharingInteractor {
        return PlayerSharingInteractor(getSharingNavigator(context))
    }

    private fun getSettingsRepository(context: Context): SettingsRepository {
        return PlayerSettingsRepository(
            SettingsLocalStorage(context.getSharedPreferences("APP_SETTINGS", Context.MODE_PRIVATE))
        )
    }

    fun provideSettingsInteractor(context: Context): SettingsInteractor {
        return PlayerSettingsInteractor(getSettingsRepository(context))
    }
}