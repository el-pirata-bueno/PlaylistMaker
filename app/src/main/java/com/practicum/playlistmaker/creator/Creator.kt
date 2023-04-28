package com.practicum.playlistmaker.creator

import com.practicum.playlistmaker.data.network.NetworkClient
import com.practicum.playlistmaker.data.player.PlayerRepository
import com.practicum.playlistmaker.domain.impl.PlayerInteractor
import com.practicum.playlistmaker.presentation.player.PlayerPresenter
import com.practicum.playlistmaker.presentation.player.PlayerScreenView

object Creator {
    private fun getRepository(): PlayerRepository {
        return PlayerRepository(NetworkClient())
    }

    fun providePresenter(view: PlayerScreenView, trackId: String): PlayerPresenter {
        return PlayerPresenter(
            view = view,
            trackId = trackId,
            playerInteractor = PlayerInteractor(getRepository())
        )
    }
}