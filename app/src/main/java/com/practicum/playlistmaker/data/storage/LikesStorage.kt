package com.practicum.playlistmaker.data.storage

interface LikesStorage {

    fun likeTrack(trackId: Int)
    fun unlikeTrack(trackId: Int)
    fun getLiked(): Set<String>

}