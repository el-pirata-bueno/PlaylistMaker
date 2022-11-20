package com.practicum.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchButton = findViewById<Button>(R.id.search)
        val mediaButton = findViewById<Button>(R.id.media)
        val settingsButton = findViewById<Button>(R.id.settings)

        searchButton.setOnClickListener {
            val searchIntent = Intent(this, SearchActivity::class.java)
            startActivity(searchIntent)
        }

        mediaButton.setOnClickListener {
            val mediaIntent = Intent(this, MediaActivity::class.java)
            startActivity(mediaIntent)
        }

        settingsButton.setOnClickListener {
            val settingsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingsIntent)
        }

        /*val searchButtonListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                Toast.makeText(this@MainActivity, "Поиск в разработке", Toast.LENGTH_SHORT).show()
            }
        }

        val mediaButtonListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                Toast.makeText(this@MainActivity, "Медиатека в разработке", Toast.LENGTH_SHORT).show()
            }
        }

        val settingsButtonListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                Toast.makeText(this@MainActivity, "Настройки в разработке", Toast.LENGTH_SHORT).show()
            }
        }

        searchButton.setOnClickListener(searchButtonListener)
        mediaButton.setOnClickListener(mediaButtonListener)
        settingsButton.setOnClickListener(settingsButtonListener)*/

        /*searchButton.setOnClickListener {
            Toast.makeText(this@MainActivity, "Поиск в разработке", Toast.LENGTH_SHORT).show()
        }

        mediaButton.setOnClickListener {
            Toast.makeText(this@MainActivity, "Медиатека в разработке", Toast.LENGTH_SHORT).show()
        }

        settingsButton.setOnClickListener {
            Toast.makeText(this@MainActivity, "Настройки в разработке", Toast.LENGTH_SHORT).show()
        }*/


    }

}