package com.practicum.playlistmaker.data.sharing.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.practicum.playlistmaker.data.sharing.ExternalNavigator
import com.practicum.playlistmaker.domain.sharing.model.EmailData

class PlayerExternalNavigator(private val context: Context) : ExternalNavigator {

    override fun shareApp(shareUrl: String, typeText: String) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = typeText
            putExtra(Intent.EXTRA_TEXT, shareUrl)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(shareIntent)
    }

    override fun openSupport(email: EmailData) {
        val helpIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse(email.data)
            putExtra(Intent.EXTRA_EMAIL, arrayOf(email.emailAddress))
            putExtra(Intent.EXTRA_SUBJECT, email.emailSubject)
            putExtra(Intent.EXTRA_TEXT, email.emailText)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(helpIntent)
    }


    override fun openTerms(link: String) {
        val agreementIntent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(link)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(agreementIntent)
    }
}