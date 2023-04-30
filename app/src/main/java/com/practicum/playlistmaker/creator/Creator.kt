package com.practicum.playlistmaker.creator

import android.content.SharedPreferences
import com.practicum.playlistmaker.data.network.ITunesNetworkClient
import com.practicum.playlistmaker.data.player.TrackPlayerRepository
import com.practicum.playlistmaker.data.search.TrackSearchHistory
import com.practicum.playlistmaker.data.search.TrackSearchRepository
import com.practicum.playlistmaker.data.api.ITunesApi
import com.practicum.playlistmaker.domain.impl.TrackPlayerInteractor
import com.practicum.playlistmaker.domain.impl.TrackSearchInteractor
import com.practicum.playlistmaker.presentation.player.PlayerPresenter
import com.practicum.playlistmaker.presentation.player.PlayerScreenView
import com.practicum.playlistmaker.presentation.search.SearchPresenter
import com.practicum.playlistmaker.presentation.search.SearchRouter
import com.practicum.playlistmaker.presentation.search.SearchScreenView
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Creator {

    private val iTunesBaseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private fun getPlayerRepository(): TrackPlayerRepository {
        return TrackPlayerRepository(ITunesNetworkClient())
    }

    private fun getSearchRepository(): TrackSearchRepository {
        return TrackSearchRepository(retrofit.create(ITunesApi::class.java))
    }

    private fun getSearchHistory(sharedPrefsSearchHistory: SharedPreferences): TrackSearchHistory {
        return TrackSearchHistory(sharedPrefsSearchHistory)
    }

    fun providePlayerPresenter(view: PlayerScreenView, trackId: String): PlayerPresenter {
        return PlayerPresenter(
            view = view,
            trackId = trackId,
            playerInteractor = TrackPlayerInteractor(getPlayerRepository()),
        )
    }

    fun provideSearchPresenter(
        view: SearchScreenView,
        router: SearchRouter,
        sharedPrefsSearchHistory: SharedPreferences
    ): SearchPresenter {
        return SearchPresenter(
            view = view,
            router = router,
            searchInteractor = TrackSearchInteractor(
                getSearchHistory(sharedPrefsSearchHistory),
                getSearchRepository()
            )
        )
    }
}