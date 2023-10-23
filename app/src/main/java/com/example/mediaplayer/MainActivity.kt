package com.example.mediaplayer

import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.postDelayed
import com.example.mediaplayer.databinding.ActivityMainBinding
import java.lang.Exception


class MainActivity : AppCompatActivity() {
    private var currIndex = 0
    lateinit var firstScreen: ActivityMainBinding
    lateinit var runnadle : Runnable
    private var handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firstScreen = ActivityMainBinding.inflate(layoutInflater)
        setContentView(firstScreen.root)
        //Присвоение трекам порядковых номеров
        val song = arrayListOf<Int>()
        song.add(0, R.raw.track_1)
        song.add(1, R.raw.track_2)
        song.add(2, R.raw.track_3)

        var mediaPlayer = MediaPlayer.create(this, song[currIndex])
        val audioManager = getSystemService(AUDIO_SERVICE) as AudioManager





        // Пауза Воспроизведение Вперед Назад
        firstScreen.play.setOnClickListener {
            if (mediaPlayer != null && mediaPlayer.isPlaying) {
                mediaPlayer.pause()
                firstScreen.play.setImageResource(R.drawable.play)
            } else {
                mediaPlayer.start()
                firstScreen.play.setImageResource(R.drawable.pause)
            }
            SongName()

        }

        firstScreen.next.setOnClickListener {
            if (mediaPlayer != null) {
                firstScreen.play.setImageResource(R.drawable.pause)
            }
            if (currIndex < song.size - 1) {
                currIndex++
            }

            if (mediaPlayer.isPlaying) {
                mediaPlayer.stop()
            }
            mediaPlayer = MediaPlayer.create(this, song[currIndex])
            mediaPlayer.start()
            SongName()
        }


        firstScreen.prev.setOnClickListener {
            if (mediaPlayer != null) {
                firstScreen.play.setImageResource(R.drawable.pause)
            }

            if (currIndex > 0) {
                currIndex--
            } else {
                currIndex = song.size - 1
            }

            if (mediaPlayer.isPlaying) {
                mediaPlayer.stop()
            }
            mediaPlayer = MediaPlayer.create(this, song[currIndex])
            mediaPlayer.start()
            SongName()
        }
        //Увеличение уменьшение громкости
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        firstScreen.seekBarVol.max = maxVolume

        val currVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        firstScreen.seekBarVol.progress = currVolume

        firstScreen.seekBarVol.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, p1, 0)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }
        })

        // Перемотка трека
        firstScreen.seekBarTime.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                if(p2)  mediaPlayer.seekTo(p1)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }
        })

        runnadle = Runnable {
            firstScreen.seekBarTime.progress = mediaPlayer.currentPosition
            handler.postDelayed(runnadle,1000)
        }

        handler.postDelayed(runnadle,1000)
        mediaPlayer.setOnCompletionListener {
            firstScreen.play.setImageResource(R.drawable.play)
            firstScreen.seekBarTime.progress = 0
        }

    }

    // Наименования треков
     fun SongName() {
        if (currIndex == 0) {
            firstScreen.songTitle.text = "First Song"
        }
        if (currIndex == 1) {
            firstScreen.songTitle.text = "Second Song"
        }
        if (currIndex == 2) {
            firstScreen.songTitle.text = "Third Song"
        }

    }

}