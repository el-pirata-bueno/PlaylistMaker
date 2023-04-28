package com.practicum.playlistmaker.data.network

import com.practicum.playlistmaker.data.NetworkClientInterface
import com.practicum.playlistmaker.data.dto.RequestGetTrack

class NetworkClient : NetworkClientInterface {
    // В перспективе будет вовзращать массив треков
    override fun doRequest(getTrack: RequestGetTrack) {
        val iTunesBaseUrl = "https://itunes.apple.com/"

        //Код работы с сетью - в работе

    }
}