package com.practicum.playlistmaker.util

object Creator {

    //private fun getPlayerRepository(context: Context): TrackPlayer {
    //    return AudioTrackPlayer(
    //        RetrofitNetworkClient(context),
    //        LikesLocalStorage(context.getSharedPreferences("LIKED_TRACKS", Context.MODE_PRIVATE)),
    //        PlaylistsLocalStorage(context.getSharedPreferences("PLAYLISTS", Context.MODE_PRIVATE)),
    //    )
    //}

    //fun providePlayerInteractor(context: Context): PlayerInteractor {
    //    return TrackPlayerInteractor(getPlayerRepository(context))
    //}

    //fun provideApi(): ITunesApiService {
    //    val logging = HttpLoggingInterceptor().apply {
    //        setLevel(HttpLoggingInterceptor.Level.BODY)
    //    }
    //    val okHttpClient = OkHttpClient.Builder().addInterceptor(logging).build()
    //   return Retrofit.Builder()
    //      .baseUrl("http://itunes.apple.com/")   //можно заменить на http, чтобы работало бодрее
    //        .addConverterFactory(GsonConverterFactory.create())
    //        .client(okHttpClient).build().create(ITunesApiService::class.java)
    //}
    //

    //private fun getSearchRepository(context: Context): SearchRepository {
    //    return PlayerSearchRepository(
    //        RetrofitNetworkClient(context),
    //        LikesLocalStorage(context.getSharedPreferences("LIKED_TRACKS", Context.MODE_PRIVATE)),
    //        PlaylistsLocalStorage(context.getSharedPreferences("PLAYLISTS", Context.MODE_PRIVATE)),
    //    )
    //}

    //private fun getSearchHistory(context: Context): TracksSearchHistory {
    //    return TracksSearchHistory(
    //        HistoryLocalStorage(
    //            context.getSharedPreferences(
    //                "HISTORY_TRACKS",
    //                Context.MODE_PRIVATE
    //            )
    //        )
    //    )
    //}

    //fun provideSearchInteractor(context: Context): SearchInteractor {
    //    return PlayerSearchInteractor(getSearchRepository(context), getSearchHistory(context))
    //}

    //private fun getSharingNavigator(context: Context): ExternalNavigator {
    //    return PlayerExternalNavigator(context)
    //}

    //fun provideSharingInteractor(context: Context): SharingInteractor {
    //    return PlayerSharingInteractor(getSharingNavigator(context))
    //}

    //private fun getSettingsRepository(context: Context): SettingsRepository {
    //    return PlayerSettingsRepository(
    //        SettingsLocalStorage(context.getSharedPreferences("APP_SETTINGS", Context.MODE_PRIVATE))
    //    )
    //}

    //fun provideSettingsInteractor(context: Context): SettingsInteractor {
    //    return PlayerSettingsInteractor(getSettingsRepository(context))
    //}
}