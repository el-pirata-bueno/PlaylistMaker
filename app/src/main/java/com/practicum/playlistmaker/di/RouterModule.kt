package com.practicum.playlistmaker.di

import android.os.Looper
import com.practicum.playlistmaker.ui.models.HandlerRouter
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val routerModule = module {
    singleOf(::HandlerRouter)

    single { Looper.getMainLooper() }

}