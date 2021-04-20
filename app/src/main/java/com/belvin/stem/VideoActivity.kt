package com.belvin.stem

import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.URLUtil
import android.widget.MediaController
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView

class VideoActivity : AppCompatActivity() {

    lateinit var videoView:VideoView
    lateinit var videoPath:String
    lateinit var bufferingText:TextView
    var mCurrentPosition = 0
    companion object{
        var PLAYBACK_TIME = "play_time"
    }


    override fun onStart() {
        super.onStart()
        initializePlayer()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)

        if(savedInstanceState != null){
            mCurrentPosition = savedInstanceState.getInt(PLAYBACK_TIME)
        }

        videoView = findViewById(R.id.videoView)
        bufferingText = findViewById(R.id.buffering_textview)

        var mCon = MediaController(this)
        mCon.setMediaPlayer(videoView)
        videoView.setMediaController(mCon)
    }

    override fun onPause() {
        super.onPause()
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.N){
            videoView.pause()
        }
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(PLAYBACK_TIME,videoView.currentPosition)
    }

    fun initializePlayer(){
        videoPath = intent.extras!!.getString("VideoPath")!!
        val videoUri = getMedia(videoPath)
        Log.d( "initializePlayer: ",videoUri.toString())
        bufferingText.visibility = VideoView.VISIBLE
        //videoView.setVideoPath()
        videoView.setVideoURI(videoUri)
        videoView.setOnPreparedListener {
            bufferingText.visibility = VideoView.INVISIBLE
            if(mCurrentPosition > 0){
                videoView.seekTo(mCurrentPosition)
            }else{
                videoView.seekTo(1)
            }
            videoView.start()
        }

        videoView.setOnCompletionListener {
            Toast.makeText(applicationContext, "Video Finished", Toast.LENGTH_SHORT).show()
        }
    }

    fun getMedia(mediaName:String): Uri? {

        if(URLUtil.isValidUrl(mediaName)){
            return Uri.parse(mediaName)
        }
        //var id = resources.getIdentifier(mediaName,"id",packageName)
        return Uri.parse("android.resource://${packageName}/raw/${mediaName}")
    }

    fun releasePlayer(){
        videoView.stopPlayback()
    }
}