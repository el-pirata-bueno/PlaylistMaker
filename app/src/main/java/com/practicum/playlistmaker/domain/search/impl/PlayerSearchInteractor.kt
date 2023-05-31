package com.practicum.playlistmaker.domain.search.impl

import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.search.SearchHistory
import com.practicum.playlistmaker.domain.search.SearchInteractor
import com.practicum.playlistmaker.domain.search.SearchRepository
import com.practicum.playlistmaker.util.Resource
import java.util.concurrent.Executors

class PlayerSearchInteractor(
    private val repository: SearchRepository,
    private val searchHistory: SearchHistory
) : SearchInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun clearHistory() {
        searchHistory.clearHistory()
    }

    override fun getHistoryTracks(): List<Track> {
        return searchHistory.getHistory()
    }

    override fun addTrackToHistory(trackId: Int) {
        getOneTrack(trackId, object : SearchInteractor.GetOneTrackConsumer {
            override fun consume(foundTrack: List<Track>?, errorMessage: String?) {
                if (foundTrack != null) {
                    searchHistory.addTrackToHistory(foundTrack[0])
                }
            }
        }
        )
    }

    override fun getTracks(term: String, consumer: SearchInteractor.GetTracksConsumer) {
        executor.execute {
            when (val resource = repository.searchTracks(term)) {
                is Resource.Success -> {
                    consumer.consume(resource.data, null)
                }

                is Resource.Error -> {
                    consumer.consume(null, resource.message)
                }
            }
        }
    }

    override fun getOneTrack(trackId: Int, consumer: SearchInteractor.GetOneTrackConsumer) {
        executor.execute {
            when (val resource = repository.getTrack(trackId)) {
                is Resource.Success -> {
                    consumer.consume(resource.data, null)
                }

                is Resource.Error -> {
                    consumer.consume(null, resource.message)
                }
            }
        }
    }
}
