package com.practicum.playlistmaker.di

import android.os.Looper
import com.practicum.playlistmaker.ui.models.HandlerRouter
import com.practicum.playlistmaker.ui.models.NavigationRouter
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val routerModule = module {
    singleOf(::HandlerRouter)
    singleOf(::NavigationRouter)

    single { Looper.getMainLooper() }

}