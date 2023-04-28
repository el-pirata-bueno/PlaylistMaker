package com.practicum.playlistmaker.data

import com.practicum.playlistmaker.data.dto.RequestGetTrack

interface NetworkClientInterface {
    // В перспективе будет вовзращать массив треков
    fun doRequest(getTrack: RequestGetTrack)
}