package com.practicum.playlistmaker.data.storage

interface HistoryStorage {

    fun getHistory(): String
    fun saveHistory(json: String)
    fun clearHistory()

}